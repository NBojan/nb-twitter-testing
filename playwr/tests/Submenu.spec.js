import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Submenu.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
    viewport: { width: 600, height: 900 }
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("051-ValidateChoosingTheUploadProfilePictureClosesTheSubmenu", async ({ page }) => {
    await page.locator("//button[@id='sidebarButton']").click();
    await page.locator("aside.bg-gray-100").getByRole("button", { name: "Upload profile picture" }).click();
    const submenuClasses = await page.locator("aside.bg-gray-100").getAttribute("class");
    expect(submenuClasses).toContain("opacity-0");
    expect(submenuClasses).toContain("-translate-x-full");
});

test("052-ValidateChoosingTheUploadProfilePictureOpensTheUploadProfilePictureContainer", async ({ page }) => {
    await page.locator("//button[@id='sidebarButton']").click();
    await page.locator("aside.bg-gray-100").getByRole("button", { name: "Upload profile picture" }).click();
    await expect(page.locator("aside").filter({ hasText: "Select a Picture" })).toBeVisible();
});

test("053-ValidateClosingTheSubmenuWithTheCloseButton", async ({ page }) => {
    await page.locator("//button[@id='sidebarButton']").click();
    await page.locator("aside.bg-gray-100").locator("div.border-gray-300").getByRole("button").click();
    const submenuClasses = await page.locator("aside.bg-gray-100").getAttribute("class");
    expect(submenuClasses).toContain("opacity-0");
    expect(submenuClasses).toContain("-translate-x-full");
});

test("054-ValidateTheSignOut", async ({ page }) => {
    await page.locator("//button[@id='sidebarButton']").click();
    await page.locator("aside.bg-gray-100").getByRole("button", { name: "Sign Out" }).click();
    await expect(page.getByRole("heading", { name: "Sign Up" })).toBeVisible();
});
