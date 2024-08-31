import { test, expect } from "@playwright/test";
import { url, dummyProfileUrl } from "../utils/urls";

const helper = "npx playwright test tests/Sidemenu.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("026-VerifyTheTwitterLogoIsVisible", async ({ page }) => {
    await expect(page.getByAltText("Logo")).toBeVisible();
});

test("027-VerifyTheTwitterLogoIsALinkToTheHomepage", async ({ page }) => {
    await page.goto(dummyProfileUrl);
    await page.getByAltText("Logo").click();
    expect(page.url()).toMatch(url);
});

test("028-VerifyTheLinksExistInTheSidemenu", async ({ page }) => {
    await expect(page.locator("#leftSidebarLinks")).toBeVisible();
});

test("029-VerifyTheTweetButtonIsVisible", async ({ page }) => {
    await expect(page.locator("#leftSidebar").getByRole("button", { name: "Tweet" })).toBeVisible();
});

test("030-VerifyTheProfileMenuIsVisible", async ({ page }) => {
    await expect(page.locator(".userProfile")).toBeVisible();
});

test("031-VerifyTheSidemenuDisappearsInScreensBelow640pxWidth", async ({ page }) => {
    await page.setViewportSize({
        width: 600,
        height: 840,
    });
    await expect(page.locator("#leftSidebar")).toBeHidden();
});