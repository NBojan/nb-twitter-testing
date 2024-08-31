import { url } from "../utils/urls";
import { test, expect } from "@playwright/test";

const helper = "npx playwright test tests/Likes.spec.js --project=chromium";

test.use({
    storageState: "storage/user.json",
})

test.beforeEach( async ({ page }) => {
    await page.goto(url);
});

// dummies and mytestingaccount need to have NO likes on ownPost and anotherUserPost to begin with.

const alwaysLikedPost = "//p[text()='PostThatWillAlwaysBeLiked']/ancestor::article";
const ownPostLocator = "//p[text()='TestingPurposesPostDontTouch']/ancestor::article";
// const ownPostLocator = "//p[text()='Hello from me dummies.']/ancestor::article"; //for dummmy account test
const anotherUserPostLocator = "//p[text()='Forza Milan Sempre!!']/ancestor::article";

test("070-ValidateLikingAndRemovingLikeOnAPostFromAnotherUser @smoke", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);
    await tweet.locator("button.like-btn").click();
    await expect(tweet.locator("button.dislike-btn")).toBeVisible();

    await tweet.locator("button.dislike-btn").click();
    await expect(tweet.locator("button.like-btn")).toBeVisible();
});

test("071-ValidateLikingAndRemovingLikeOnAPersonalPost @smoke", async ({ page }) => {
    const tweet = page.locator(ownPostLocator);
    await tweet.locator("button.like-btn").click();
    await expect(tweet.locator("button.dislike-btn")).toBeVisible();

    await tweet.locator("button.dislike-btn").click();
    await expect(tweet.locator("button.like-btn")).toBeVisible();
});

test("072-ValidateLikingAndRemovingTheLikeAfterGoingToThePostPage @smoke", async ({ page }) => {
    const tweet = page.locator(anotherUserPostLocator);

    await tweet.locator("p.post-text").click();

    await tweet.locator("button.like-btn").click();
    await expect(tweet.locator("button.dislike-btn")).toBeVisible();

    await tweet.locator("button.dislike-btn").click();
    await expect(tweet.locator("button.like-btn")).toBeVisible();
});

test("073-ValidateLikingAndRemovingTheLikeAfterGoingToTheProfilePage @smoke", async ({ page }) => {
    const tweet = page.locator(ownPostLocator);

    await tweet.locator("p.post-username").click();

    await tweet.locator("button.like-btn").click();
    await expect(tweet.locator("button.dislike-btn")).toBeVisible();

    await tweet.locator("button.dislike-btn").click();
    await expect(tweet.locator("button.like-btn")).toBeVisible();
});

test("074-VerifyNumberOfLikesIsDisplayedForAPostThatHasAtLeast1Like @smoke", async ({ page }) => {
    const tweet = page.locator(alwaysLikedPost);
    await expect(tweet.locator("span.likeNumber")).toBeVisible();
});
test("075-VerifyTheNumberOfLikesIsHiddenIfThereAreNoLikes", async ({ page }) => {
    const tweet = page.locator(ownPostLocator);
    await expect(tweet.locator("span.likeNumber")).toHaveCount(0);
});