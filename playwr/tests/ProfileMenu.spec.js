import { test, expect } from "@playwright/test";
import { url } from "../utils/urls";

const helper = "npx playwright test tests/ProfileMenu.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("032-VerifyUploadProfilePictureIsVisible", async ({ page }) => {
    await page.locator(".userProfile").click();
    await expect(page.locator('#leftSidebar').getByRole('button', { name: 'Upload profile picture' })).toBeVisible();
});

test("033-ValidateTheSignOut @smoke", async ({ page }) => {
    await page.locator(".userProfile").click();
    await expect(page.locator('#leftSidebar').getByRole('button', { name: 'Sign Out' })).toBeVisible();
    await page.locator('#leftSidebar').getByRole('button', { name: 'Sign Out' }).click();
    await expect(page.getByRole("heading", { name: "Sign Up" })).toBeVisible();
});