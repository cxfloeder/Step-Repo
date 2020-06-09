// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

const MESSAGE_API_URL = "/data";
const PASSWORD = "CoolPeopleOnly";
/**
 * Adds a random greeting to the page.
 */
function addRandomFact() {
    const facts =
        ['My favorite color is green.',
            'I like playing music (Piano, Guitar, Drums)',
            "I'm good at bowling.",
            'There are 10 kids in my family.',
            'I drive a blue Tacoma.',
            'I drive a blue Tacoma.',
            'I have a DQ visor.',
            'I enjoy playing chess',
            'My favorite number is 17',
            'I play the buckets.',
            "I'm an uncle.",
            'I used to have a buzz cut.',
            'I got my first phone when I was 18.',
            'I like to jump-rope.'];

    // Pick a random greeting.
    const response = facts[Math.floor(Math.random() * facts.length)];

    // Add it to the page.
    const greetingContainer = document.getElementById('greeting-container');
    greetingContainer.innerText = response;
}

/** Fetch and display the desired number of comments to the portfolio. */
async function displayComments() {
    const numComments = document.getElementById('num-comments').value;
    const response = await fetch('/data?num-comments=' + numComments);
    const messageArr = await response.json();

    // Split messageArr into paragraph elements
    var output = messageArr.map(str => "<p>" + str.email + ": " + str.message + "</p>");
    document.getElementById('comment_section').innerHTML = output.join("");
}

/** 
 * Check if user entered the correct password. The password isn't protected
 * or secure, just a fun feature for learning purposes.
 */
function validatePassword(form) {
    var commentPassword = form.password.value;

    if (commentPassword != PASSWORD) {
        alert("Sorry, incorrect password.");
    }
    else {
        alert("Access Granted.");
        deleteComments();
    }
}

function isLoggedIn() {
}

/** Delete the entire datastore. */
function deleteComments() {
    fetch('/delete-data');
}
