# YouTube Automation Test Cases
This project automates the testing of YouTube's main sections using Selenium WebDriver and TestNG. It includes test cases that verify different functionalities and UI elements on YouTube, such as page load validation, content checks, and interaction with the site.

Description
The main goal of this project is to automate functional testing on YouTube's website. The tests are designed to ensure that the site behaves as expected when interacting with various sections, such as "Movies," "Music," "News," and more.

Key Features:
Validate the correct URL on the YouTube homepage.
Verify that movies are marked with the correct category and rating.
Check that the music section loads correctly and validate playlist details.
Accumulate and validate likes for the top news posts.
Search for a keyword and accumulate total likes based on video view counts.
Prerequisites
Before running the tests, ensure you have the following installed:

Java 8 or above
Selenium WebDriver
TestNG
ChromeDriver
Maven (for dependency management)
Installation
Clone the repository:
git clone https://github.com/yourusername/youtube-automation.git
Navigate to the project directory:
cd youtube-automation
Install dependencies using Maven:
mvn install
Run the tests:
mvn test
Test Cases
testCase01: Verify Homepage URL and About Page
This test checks if the current URL matches YouTube's homepage URL and then navigates to the "About" page. It also prints the content of the "About" page.

testCase02: Verify Movie Category and Rating
This test clicks on the "Movies" section, navigates through the top-selling movies, and checks if the last movie is rated "A" (for mature audiences). It also verifies the movie's category.

testCase03: Verify Music Playlist Title Count
This test opens the "Music" section, clicks the "Show more" button, and validates if the title count of the last playlist is less than or equal to 50.

testCase04: Verify Total Likes for News Posts
This test validates the like count for the first 3 news posts on YouTube's "News" section and accumulates the total likes.

testCase05: Search for a Keyword and Accumulate Likes
This test searches for a specified keyword (from Excel data), iterates through the search results, and accumulates the total likes based on the views of the videos.











