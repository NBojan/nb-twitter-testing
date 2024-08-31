import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/PostPage.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

const postWithComments = "https://nb-twitter.vercel.app/tweet/WZMIjKLqQV2pIIpFJ22q";
const postWithoutComments = "https://nb-twitter.vercel.app/tweet/0eEQirDmCBRRg0M6t7lX";

test("094-ValidateLinkInTheHeaderRedirectsToTheHomepage", async ({ page }) => {
    const mainSection = page.locator("section.flex-1");
    await page.goto(postWithComments);
    await page.locator("header").getByRole("link").click();
    await expect(mainSection.locator("textarea[id='main-textarea']")).toBeVisible();
    await expect(page.url()).toMatch(url);
});

test("095-VerifyTheRepliesArePostedForAPostThatHasReplies", async ({ page }) => {
    await page.goto(postWithComments);
    const commentsContainer = page.locator("//h5[text()='Replies']/parent::div");
    await expect(commentsContainer.locator("//p[text()='Be the first to leave a reply.']")).toHaveCount(0);
    await expect(commentsContainer.locator("article").first()).toBeVisible();
    await expect(await commentsContainer.locator("article").count()).toBeGreaterThan(0);
});

test("096-VerifyTheProvidedMessageAppersForPostsWithNoComments", async ({ page }) => {
    await page.goto(postWithoutComments);
    const commentsContainer = page.locator("//h5[text()='Replies']/parent::div");
    await expect(commentsContainer.locator("//p[text()='Be the first to leave a reply.']")).toBeVisible();
});