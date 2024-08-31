import path from "path";
import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";
import { randomNumber } from "../utils/randomText";

const helper = "npx playwright test tests/UploadProfilePicture.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test.describe("run in debug mode if it fails, give it more time to upload before heading to the next step", () => {
    test("034-ValidateProfilePictureChangeByUsingAValidImage @smoke", async ({ page }) => {
        const rndNumb = randomNumber(2);
        const imageBeforeChange = await page.locator("#leftSidebar").getByAltText("UserImg").getAttribute("src");
        await page.locator(".userProfile").click();
        await page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' }).click();
        const profilePictureModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
        await expect(profilePictureModal).toBeVisible();
    
        const fileChooserPromise = page.waitForEvent('filechooser');
        await profilePictureModal.locator("//label[text()='Select a Picture']").click();
        const fileChooser = await fileChooserPromise;
        await fileChooser.setFiles(path.join(__dirname, `../images/profilePics/${rndNumb}.jpg`));
        await profilePictureModal.getByRole("button", { name: "Upload" }).click();
        await page.waitForTimeout(2000)
        await expect(page.locator("aside").filter({ hasText: "Select a Picture" })).toBeHidden();
        await page.reload();
        
        const imageAfterChange = await page.locator("#leftSidebar").getByAltText("UserImg").getAttribute("src");
        await expect(imageBeforeChange).not.toMatch(imageAfterChange);
    });
})

test("035-ValidateProfilePicturePreviewAfterSelectingAnImage", async ({ page }) => {
    await page.locator(".userProfile").click();
    await page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' }).click();
    const profilePictureModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(profilePictureModal).toBeVisible();

    const fileChooserPromise = page.waitForEvent('filechooser');
    await profilePictureModal.locator("//label[text()='Select a Picture']").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/profilePics/1.jpg`));

    await expect(profilePictureModal.getByAltText("selectedFile")).toBeVisible();
});

test("036-ValidateRemovingASelectedImageAndSelectAPictureAgain", async ({ page }) => {
    await page.locator(".userProfile").click();
    await page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' }).click();
    const profilePictureModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(profilePictureModal).toBeVisible();

    const fileChooserPromise = page.waitForEvent('filechooser');
    await profilePictureModal.locator("//label[text()='Select a Picture']").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/profilePics/1.jpg`));
    await expect(profilePictureModal.getByAltText("selectedFile")).toBeVisible();
   
    await profilePictureModal.locator("//img[@alt='selectedFile']/parent::div").click();
    await expect(profilePictureModal.getByAltText("selectedFile")).toHaveCount(0);

    const fileChooserPromiseTwo = page.waitForEvent('filechooser');
    await profilePictureModal.locator("//label[text()='Select a Picture']").click();
    const fileChooserTwo = await fileChooserPromiseTwo;
    await fileChooserTwo.setFiles(path.join(__dirname, `../images/profilePics/1.jpg`));
    await expect(profilePictureModal.getByAltText("selectedFile")).toBeVisible();
});

test("037-ValidateUploadButtonDisabledIfNoImageSelected", async ({ page }) => {
    await page.locator(".userProfile").click();
    await page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' }).click();
    const profilePictureModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(profilePictureModal).toBeVisible();

    await expect(profilePictureModal.getByRole("button", { name: "Upload" })).toBeDisabled();
});

test.skip("038-ValidateUploadButtonDisabledIfWrongFormatSelected", async ({ page }) => {
    await page.locator(".userProfile").click();
    await page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' }).click();
    const profilePictureModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(profilePictureModal).toBeVisible();

    const fileChooserPromise = page.waitForEvent('filechooser');
    await profilePictureModal.locator("//label[text()='Select a Picture']").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/wrongFormat/1.pdf`));

    await expect(profilePictureModal.getByRole("button", { name: "Upload" })).toBeDisabled();
});

test("039-ValidateCloseButtonClosesTheUploadContainer @smoke", async ({ page }) => {
    await page.locator(".userProfile").click();
    await page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' }).click();
    const profilePictureModal = page.locator("//aside[contains(@id, 'closeModal') and contains(@class, 'z-50')]");
    await expect(profilePictureModal).toBeVisible();

    await profilePictureModal.locator("button.rounded-full").click();

    await expect(page.locator("#closeModal").filter({ hasText: "Select a Picture" })).toBeHidden();
});