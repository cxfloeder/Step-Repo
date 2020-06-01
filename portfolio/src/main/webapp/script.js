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

/** Uses fetch() to add a message to the DOM. */
 function getMessage() {
    // Fetches from the DataServlet.
    const responsePromise = fetch(MESSAGR_API_URL);

    // Passes the response to handleResponse after the request is complete.
    responsePromise.then(handleResponse);
 }

/**
 * Handles response by converting it to text and passing the result to
 * addQuoteToDom().
 */
 function handleResponse(response) {
    const textPromise = response.text();
    textPromise.then(addMessageToDom);
 }

/** Adds the message to the DOM. */
function addMessageToDom(message) {
    const messageContainer = document.getElementById('message_container');
    messageContainer.innerText = message;
}

/** Practices using arrow functions to shorten the code. */
function getMessageUsingArrowFunctions() {
    fetch(MESSAGE_API_URL).then(response => response.text()).then((message) => {
        document.getElementById('message_container').innerText = message;
    });
}

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
