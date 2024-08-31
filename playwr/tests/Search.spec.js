import { url, searchUrl } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Search.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

const validUsername = "dummies";
const invalidUsername = "iDontExist";

test.skip("044-ValidateSerchingForAnExistingUsername", async ({ page }) => {
    await page.locator("input[name='search']").fill(validUsername);
    await page.keyboard.press("Enter");
    await expect(page.url()).toMatch(`${searchUrl}/${validUsername}`);
});

test.skip("045-ValidateSearchingForANonExistingUsername", async ({ page }) => {
    await page.locator("input[name='search']").fill(invalidUsername);
    await page.keyboard.press("Enter");
    await expect(page.url()).toMatch(`${searchUrl}/${invalidUsername}`);
});
