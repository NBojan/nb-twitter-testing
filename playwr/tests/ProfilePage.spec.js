import { url, myTestingAccountUrl } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/ProfilePage.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(myTestingAccountUrl);
});

test("102-ValidateLinkInTheHeaderRedirectsToTheHomepage", async ({ page }) => {
    await page.locator("header").getByRole("link").click();
    await expect(page.locator("section.flex-1").locator("#main-textarea")).toBeVisible();
    await expect(page.url()).toMatch(url);
});

test("103-VerifyTheUsersPostsAreVisible", async ({ page }) => {
    const postsContainer = page.locator("//h5[text()='Tweets']/parent::div");
    await expect(postsContainer.locator("article").first()).toBeVisible();
    await expect(await postsContainer.locator("article").count()).toBeGreaterThan(0);
});

test("104-VerifyTheProvidedMessageAppersForUsersWithNoTweets", async ({ page }) => {
    const message = "No recent tweet activity.";
    const userUrl = "https://nb-twitter.vercel.app/profile/alexo";
    await page.goto(userUrl);
    const postsContainer = page.locator("//h5[text()='Tweets']/parent::div");
    await expect(postsContainer.getByText(message)).toBeVisible();
});

test("105-VerifyTheProfilePicutreUsernameNameAndNumberOfPostsAreDisplayed", async ({ page }) => {
    await expect(page.locator("p.profile-name")).toBeVisible();
    await expect(page.locator("p.profile-username")).toBeVisible();
    await expect(page.locator("p.profile-postLength")).toBeVisible();
    await expect(page.locator("section.flex-1 div.p-3").getByAltText("UserImg")).toBeVisible();
});

