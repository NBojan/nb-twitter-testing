import { myTestingAccountUrl } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/NotFound.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(myTestingAccountUrl);
});

const badUserUrl = "https://nb-twitter.vercel.app/profile/whatwhat";
const badUrl = "https://nb-twitter.vercel.app/whatwhat";
const badPostUrl = "https://nb-twitter.vercel.app/tweet/whatwhat";

test("106-ValidateNotFoundPageAppearsWithMessageAfterSearchingForANonExistentUser", async ({ page }) => {
    const message = "Could not find that user";
    await page.goto(badUserUrl);
    await expect(page.locator(`section.flex-1 h4`)).toBeVisible();
    await expect(await page.locator(`section.flex-1 h4`).textContent()).toContain(message);
    await expect(page.locator("section.flex-1").getByRole("link", { name: "Home" })).toBeVisible();
});

test("107-ValidateNotFoundPageAppearsWithMessageAfterEnteringWrongUrl", async ({ page }) => {
    const message = "Could not find what you were looking for";
    await page.goto(badUrl);
    await expect(page.locator(`section.flex-1 h4`)).toBeVisible();
    await expect(await page.locator(`section.flex-1 h4`).textContent()).toContain(message);
    await expect(page.locator("section.flex-1").getByRole("link", { name: "Home" })).toBeVisible();
});
test("108-ValidateNotFoundPageAppearsWithMessageAfterSearchingForANonExistantPost", async ({ page }) => {
    const message = "Could not find that tweet";
    await page.goto(badPostUrl);
    await expect(page.locator(`section.flex-1 h4`)).toBeVisible();
    await expect(await page.locator(`section.flex-1 h4`).textContent()).toContain(message);
    await expect(page.locator("section.flex-1").getByRole("link", { name: "Home" })).toBeVisible();
});