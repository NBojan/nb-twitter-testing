import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Theme.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("049-ValidateTogglingTheTheme", async ({ page }) => {
    const htmlElement = page.locator("html");
    await (page.locator("//button[@id='themeButton']")).click();
    expect(await htmlElement.getAttribute("class")).toContain("dark");
    await (page.locator("//button[@id='themeButton']")).click();
    expect(await htmlElement.getAttribute("class")).not.toContain("dark");
});

test("050-ValidateTheChosenThemeStaysAfterRefreshingThePage", async ({ page }) => {
    const htmlElement = page.locator("html");
    await (page.locator("//button[@id='themeButton']")).click();
    expect(await htmlElement.getAttribute("class")).toContain("dark");
    await page.reload();
    expect(await htmlElement.getAttribute("class")).toContain("dark");
});