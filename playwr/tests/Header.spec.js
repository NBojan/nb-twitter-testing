import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Header.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("046-ValidateTheLinkIsGoingBackToTheHomepage", async ({ page }) => {
    await page.locator("header").getByRole("link", { name: "Home" }).click();
    await expect(page.url()).toMatch(url);
});

test("047-ValidateTheThemeButtonIsVisible", async ({ page }) => {
    await expect(page.locator("//button[@id='themeButton']")).toBeVisible();
});

test("048-VerifyThereIsASubmenuButtonOnScreensBelow640pxWidth @smoke", async ({ page }) => {
    await page.setViewportSize({
        width: 600,
        height: 800
    });
    await expect(page.locator("//button[@id='sidebarButton']")).toBeVisible();
});