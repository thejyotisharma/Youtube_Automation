# YouTube Automation Tests
This project automates testing for YouTube using Selenium WebDriver and TestNG. It includes test cases to verify key functionalities across different sections like homepage, movies, music, and news.

Features

Check if the homepage URL is correct.

Verify movies are properly categorized and rated.

Validate if the music section loads and playlist title count is under 50.

Accumulate likes from the top news posts.

Search for a keyword and gather total likes from video views.

Prerequisites
Before running the tests, make sure you have:

Java 8+
Selenium WebDriver
TestNG
ChromeDriver
Gradle (for dependencies)

Run the tests:

./gradlew test

Test Cases

testCase01: Verifies the homepage URL and checks the content of the "About" page.

testCase02: Validates movie ratings and categories on the "Movies" page.

testCase03: Checks if the "Music" playlist has 50 or fewer titles.

testCase04: Accumulates likes from the first 3 news posts.

testCase05: Searches for a keyword and sums likes based on video views.









