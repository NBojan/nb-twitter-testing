import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Widgets.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json"
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

test("040-VerifyTheSearchBarIsVisible", async ({ page }) => {
    await expect(page.locator("input[name='search']")).toBeVisible();
});

test("041-VerifyTheWhatIsHappeningBoxIsVisible", async ({ page }) => {
    await expect(page.locator(".bg-widget").filter({ hasText: "What's happening?" })).toBeVisible();
});

test("042-VerifyTheFollowSuggestionsBoxIsVisible", async ({ page }) => {
    await expect(page.locator(".bg-widget").filter({ hasText: "Who to follow?" })).toBeVisible();
});

test("043-VerifyTheWholeContainerIsNotVisibleBelow1024pxScreenWidth", async ({ page }) => {
    await page.setViewportSize({
        width: 800,
        height: 800
    })
    await expect(page.locator("#rightSidebar")).toBeHidden();
});