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

    // Split messageArr into paragraph elements.
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

/** Delete the entire datastore. */
function deleteComments() {
    fetch('/delete-data');
}

async function loadLogInURL() {
    var response = await fetch('/login');
    var map = await response.json();
    console.log(map["loginURL"]);
    if (map["loginURL"] != "") {
        window.location = map["loginURL"];
    }
    else {
        window.location = "comments.html";
    }
}

async function loadLogOutURL() {
    var response = await fetch('/login');
    var map = await response.json();
    console.log(map["logoutURL"]);
    if (map["logoutURL"] != "") {
        window.location = map["logoutURL"];
    } else {
        window.location = "home.html";
    }
}

function loadChartData() {
    google.charts.load('current', {
        'packages': ['geochart']
    });

    google.charts.load('current', {
        'packages':['corechart']
    });

    google.charts.setOnLoadCallback(drawStatesMap);
    google.charts.setOnLoadCallback(drawPopulationChart);
    google.charts.setOnLoadCallback(drawSeasonChart);
}

/** Create a geo-chart with all the states I've visited */
function drawStatesMap() {
    var data = google.visualization.arrayToDataTable([
        ['State', 'Visited'],
        ['Minnesota', "Minnesota"],
        ['Wisconsin', 'Wisconsin'],
        ['Nebraska', 'Nebraska'],
        ['Florida', 'Florida'],
        ['Iowa', 'Iowa'],
        ['South Dakota', 'South Dakota'],
        ['Illinois', 'Illinois'],
        ]);

    var options = {
        region: 'US',
        displayMode: 'regions',
        resolution: 'provinces',
        width: 900,
        height: 500
    };

    var chart = new google.visualization.GeoChart(document.getElementById('states_graph'));
    chart.draw(data, options);
}

/** Create a chart mapping the population growth rate of the US */
function drawPopulationChart() {
    fetch('/pop-data').then(response => response.json()).then((growthRate) => {
        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Year');
        data.addColumn('number', 'Growth-Rate');

        Object.keys(growthRate).forEach((year) => {
            data.addRow([year, growthRate[year]]);
        });

        var options = {
            title: 'US Population Growth Rate',
            hAxis: {title: 'Year'},
            vAxis: {title: 'Growth-Rate (%)'},
            width: 900,
            height: 500
        };
        
        var chart = new google.visualization.LineChart(document.getElementById('pop_graph'));
        chart.draw(data, options);
    });
}

/** Create a chart mapping the user's favorite season */
function drawSeasonChart() {
    fetch('/season-data').then(response => response.json()).then((seasonVotes) => {
        const data = new google.visualization.DataTable();
        data.addColumn('string', 'Season');
        data.addColumn('number', 'numVotes');

        Object.keys(seasonVotes).forEach((season) => {
            data.addRow([season, seasonVotes[season]]);
        });

        var options = {
            title: 'Portfolio User\'s Favorite Season',
            pieHole: 0.4,
            width: 900,
            height: 500
        };

        var chart = new google.visualization.PieChart(document.getElementById('season_chart'));
        chart.draw(data, options);
   });
}