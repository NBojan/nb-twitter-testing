import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";
import { randomText, randomNumber } from "../utils/randomText";
import path from "path";

const helper = "npx playwright test tests/UploadMenu.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("055-ValidateUploadingAPostByUsingOnlyText @smoke", async ({ page }) => {
    const text = randomText(10);
    const mainSection = page.locator("section.flex-1");
    await mainSection.locator("textarea[id='main-textarea']").fill(text);
    await mainSection.getByRole("button", { name: "Tweet" }).click();
    await mainSection.locator("textarea[id='main-textarea']").clear();
    await expect(page.getByText(text)).toBeVisible();
});

test("056-ValidateUploadingAPostByUsingTextAndAnImage @smoke", async ({ page }) => {
    const text = randomText(10);
    const rndNumb = randomNumber(5);
    const mainSection = page.locator("section.flex-1");
    
    await mainSection.locator("textarea[id='main-textarea']").fill(text);

    const fileChooserPromise = page.waitForEvent('filechooser');
    await mainSection.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));

    await mainSection.getByRole("button", { name: "Tweet" }).click();
    await mainSection.locator("textarea[id='main-textarea']").clear();

    await expect(page.getByText(text)).toBeVisible();
    await expect(await page.locator(`//p[text()='${text}']/ancestor::article//img[@alt='postImage']`)).toBeVisible();
});

test("057-VerifyThereIsAPreviewOfTheImageAfterSelectingIt @smoke", async ({ page }) => {
    const rndNumb = randomNumber(5);
    const mainSection = page.locator("section.flex-1");
    
    const fileChooserPromise = page.waitForEvent('filechooser');
    await mainSection.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));

    await expect(page.getByAltText("selectedFile")).toBeVisible();
});

test("058-ValidateRemovingASelectedImage", async ({ page }) => {
    const rndNumb = randomNumber(5);
    const mainSection = page.locator("section.flex-1");
    
    const fileChooserPromise = page.waitForEvent('filechooser');
    await mainSection.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));

    await mainSection.locator("//img[@alt='selectedFile']/parent::div").click();
    await expect(mainSection.getByAltText("selectedFile")).toHaveCount(0);
});

test("059-ValidateUploadingAPostWithTextAfterSelectingRemovingAndSelectingAnImage @smoke", async ({ page }) => {
    const text = randomText(10);
    const rndNumb = randomNumber(5);
    const secondNumb = rndNumb === 1 ? 2 : 1;
    const mainSection = page.locator("section.flex-1");
    //inserting text
    await mainSection.locator("textarea[id='main-textarea']").fill(text);
    //inserting image
    const fileChooserPromise = page.waitForEvent('filechooser');
    await mainSection.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/postPics/${rndNumb}.jpg`));
    //removing the image
    await mainSection.locator("//img[@alt='selectedFile']/parent::div").click();
    await expect(mainSection.getByAltText("selectedFile")).toHaveCount(0);
    //inserting the image again
    const newFileChooserPromise = page.waitForEvent('filechooser');
    await mainSection.locator("label").click();
    const newFileChooser = await newFileChooserPromise;
    await newFileChooser.setFiles(path.join(__dirname, `../images/postPics/${secondNumb}.jpg`));
    await expect(mainSection.getByAltText("selectedFile")).toBeVisible();
    //uploading
    await mainSection.getByRole("button", { name: "Tweet" }).click();
    await mainSection.locator("textarea[id='main-textarea']").clear();
    //assertions
    await expect(page.getByText(text)).toBeVisible();
    await expect(page.locator(`//p[text()='${text}']/ancestor::article//img[@alt='postImage']`)).toBeVisible();
});

test("060-ValidateTweetButtonIsDisabledIfNeitherTextOrImageInputted", async ({ page }) => {
    const mainSection = page.locator("section.flex-1");
    await expect(mainSection.getByRole("button", { name: "Tweet" })).toBeDisabled();
});

test("061-ValidateTweetButtonIsDisabledIfOnlyWhiteSpacesAreInputted", async ({ page }) => {
    const mainSection = page.locator("section.flex-1");
    await mainSection.locator("textarea[id='main-textarea']").fill("  ");
    await expect(mainSection.getByRole("button", { name: "Tweet" })).toBeDisabled();
});
test.skip("062-ValidateTextboxDoesNotAcceptMoreThan280Chars", async ({ page }) => {
    const text = randomText(300);
    const mainSection = page.locator("section.flex-1");
    await mainSection.locator("textarea[id='main-textarea']").fill(text);
    await expect((await mainSection.locator("textarea[id='main-textarea']").inputValue()).length).toBeLessThanOrEqual(280);
});

test.skip("063-ValidateTweetButtonIsDisabledIfWrongFormatOfFileIsSelected", async ({ page }) => {
    const mainSection = page.locator("section.flex-1");

    const fileChooserPromise = page.waitForEvent('filechooser');
    await mainSection.locator("label").click();
    const fileChooser = await fileChooserPromise;
    await fileChooser.setFiles(path.join(__dirname, `../images/wrongFormat/1.pdf`));

    await expect(mainSection.getByRole("button", { name: "Tweet" })).toBeDisabled();
});