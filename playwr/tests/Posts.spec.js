import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Posts.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

const profileUrl = "https://nb-twitter.vercel.app/profile/Three";
const postUrl = "https://nb-twitter.vercel.app/tweet/E0bBXaNW1zYhgvY3ZMAF";

test("064-VerifyAPostIncludesAProfilePictureUsernameNameAndTime @smoke", async ({ page }) => {
    const tweet = page.locator("//p[text()='Forza Milan Sempre!!']/ancestor::article");
    await expect(tweet.getByAltText("UserImg")).toBeVisible();
    await expect(tweet.locator("p.post-username")).toBeVisible();
    await expect(tweet.locator("p.post-name")).toBeVisible();
    await expect(tweet.locator("p.post-timestamp")).toBeVisible();
});

test("065-VerifyAPostIncludesTextAndAnImage @smoke", async ({ page }) => {
    const tweet = page.locator("//p[text()='My buddy is chill, lol']/ancestor::article");
    await expect(tweet.getByAltText("postImage")).toBeVisible();
    await expect(tweet.locator("p.post-text")).toBeVisible();
});

test("066-ValidateTheUsernameIsALinkToTheUserProfile", async ({ page }) => {
    const tweet = page.locator("//p[text()='My buddy is chill, lol']/ancestor::article");
    await tweet.locator("p.post-username").click();
    await expect(page.locator("p.profile-name")).toBeVisible();
    await expect(page.url()).toMatch(profileUrl);
});

test("067-ValidateTheNameIsALinkToTheUserProfile", async ({ page }) => {
    const tweet = page.locator("//p[text()='My buddy is chill, lol']/ancestor::article");
    await tweet.locator("p.post-name").click();
    await expect(page.locator("p.profile-name")).toBeVisible();
    await expect(page.url()).toMatch(profileUrl);
});

test("068-ValidateTheTextIsALinkToThePostPage", async ({ page }) => {
    const tweet = page.locator("//p[text()='My buddy is chill, lol']/ancestor::article");
    await tweet.locator("p.post-text").click();
    await expect(page.getByRole("heading", { name: "Replies" })).toBeVisible();
    await expect(page.url()).toMatch(postUrl);
});

test("069-ValidateTheImageIsALinkToThePostPage", async ({ page }) => {
    const tweet = page.locator("//p[text()='My buddy is chill, lol']/ancestor::article");
    await tweet.getByAltText("postImage").click();
    await expect(page.getByRole("heading", { name: "Replies" })).toBeVisible();
    await expect(page.url()).toMatch(postUrl);
});