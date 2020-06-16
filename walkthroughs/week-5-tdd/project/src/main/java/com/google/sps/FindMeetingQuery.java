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

package com.google.sps;

import java.util.*;

public final class FindMeetingQuery {
    public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
        // Get necessary info from the MeetingRequest object (HashSets).
        Collection<String> requiredAttendees = request.getAttendees();
        long duration = request.getDuration();

        // Get all the timeRanges where required attendees have scheduled meetings.
        ArrayList<TimeRange> relevantTimes = new ArrayList<TimeRange>();
        relevantTimes = getReleventTimes(events, requiredAttendees);

        // sort the event time intervals
        relevantTimes = sortTimeRanges(relevantTimes);

        // Find times that the required attendees can meet.
        return findMeetingTimes(relevantTimes, duration);
    }

    /**
     * Return all the times where required attendees have scheduled meetings.
     */
    private ArrayList<TimeRange> getReleventTimes(Collection<Event> events, Collection<String> requiredAttendees) {
        // don't know exactly what to put after the equals sign
        ArrayList<TimeRange> relevantTimes = new ArrayList<TimeRange>();

        for(Event event : events) {
            if(isScheduled(event, requiredAttendees)) {
                relevantTimes.add(event.getWhen());
            }
        }
        return relevantTimes;
    }

    /**
     * Check if an event has a required attendee scheduled for it.
     */
    private boolean isScheduled(Event event, Collection<String> requiredAttendees) {
        Set<String> eventAttendees = event.getAttendees();

        for(String requiredAttendee : requiredAttendees) {
            if(eventAttendees.contains(requiredAttendee)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Orders TimeRanges by their start-times in ascending order.
     */
    private ArrayList<TimeRange> sortTimeRanges(ArrayList<TimeRange> relevantTimes) {
        Collections.sort(relevantTimes, TimeRange.ORDER_BY_START);
        return relevantTimes;
    }

    /**
     * Find times that the required attendees can meet.
     */
    private Collection<TimeRange> findMeetingTimes(Collection<TimeRange> relevantTimes, long duration) {
        Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();
        
        int startTime = TimeRange.START_OF_DAY;
        int endTime = TimeRange.END_OF_DAY;

        // Add available meeting times to the List.
        for(TimeRange timeRange : relevantTimes) {
            endTime = timeRange.start();
            if(endTime - startTime >= duration) {
                meetingTimes.add(TimeRange.fromStartEnd(startTime, endTime, false));
            }
            // Prevent failure for nested events.
            if(startTime < timeRange.end()) {
                startTime = timeRange.end();
            }
        }
        // Add the last meeting time if possible. 
        if(TimeRange.END_OF_DAY - startTime >= duration) {
            meetingTimes.add(TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true));
        }
        return meetingTimes;
    }
}

