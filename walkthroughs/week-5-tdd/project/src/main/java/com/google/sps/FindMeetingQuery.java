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
        // Get necessary info from the MeetingRequest object.
        Collection<String> requiredAttendees = request.getAttendees();
        Collection<String> optionalAttendees = request.getOptionalAttendees();
        long duration = request.getDuration();

        // Get all the timeRanges where the attendees have scheduled meetings.
        ArrayList<TimeRange> unvailableTimeForRequiredAttendees = 
            getAttendeesScheduledMeetingTimes(events, requiredAttendees);
        ArrayList<TimeRange> unvailableTimeForOptionalAttendees = 
            getAttendeesScheduledMeetingTimes(events, optionalAttendees);

        ArrayList<TimeRange> unvailableTimeForAllAttendees = 
            new ArrayList<TimeRange>(unvailableTimeForRequiredAttendees);
        unvailableTimeForAllAttendees.addAll(unvailableTimeForOptionalAttendees);

        // Sort the meeting time intervals.
        unvailableTimeForRequiredAttendees = sortByStartTime(unvailableTimeForRequiredAttendees);
        unvailableTimeForAllAttendees = sortByStartTime(unvailableTimeForAllAttendees);

        // Find times where the attendees can meet.
        Collection<TimeRange> requiredMeetings = new ArrayList<TimeRange>();
        if(unvailableTimeForRequiredAttendees.size() != 0) {
            requiredMeetings = findAvailableMeetingTimes(unvailableTimeForRequiredAttendees, duration);
        }
        Collection<TimeRange> optionalMeetings = findAvailableMeetingTimes(unvailableTimeForAllAttendees, duration);

        if(optionalMeetings.size() != 0) {
            return optionalMeetings;
        }
        return requiredMeetings;
    }

    /**
     * Return all the times where attendees have scheduled meetings.
     */
    private ArrayList<TimeRange> getAttendeesScheduledMeetingTimes
        (Collection<Event> events, Collection<String> attendees) {
            
        ArrayList<TimeRange> meetingTimes = new ArrayList<TimeRange>();

        for(Event event : events) {
            if(isAnyAttendeeScheduledForEvent(event, attendees)) {
                meetingTimes.add(event.getWhen());
            }
        }
        return meetingTimes;
    }

    /**
     * Check if any attendee from the Collection is scheduled for the given event.
     */
    private boolean isAnyAttendeeScheduledForEvent(Event event, Collection<String> attendees) {
        Set<String> eventAttendees = event.getAttendees();

        for(String attendee : attendees) {
            if(eventAttendees.contains(attendee)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Orders TimeRanges by their start-times in ascending order.
     */
    private ArrayList<TimeRange> sortByStartTime(ArrayList<TimeRange> relevantTimes) {
        Collections.sort(relevantTimes, TimeRange.ORDER_BY_START);
        return relevantTimes;
    }

    /**
     * Find times that the attendees can meet given a sorted collection of unavailable meeting times.
     */
    private Collection<TimeRange> findAvailableMeetingTimes
        (Collection<TimeRange> unavailableTimes, long minimumDuration) {

        Collection<TimeRange> meetingTimes = new ArrayList<TimeRange>();
        
        int startTime = TimeRange.START_OF_DAY;
        int endTime = TimeRange.END_OF_DAY;

        // Add available meeting times to the List.
        for(TimeRange timeRange : unavailableTimes) {
            endTime = timeRange.start();
            if(endTime - startTime >= minimumDuration) {
                meetingTimes.add(TimeRange.fromStartEnd(startTime, endTime, false));
            }
            // Prevent failure for nested events.
            if(startTime < timeRange.end()) {
                startTime = timeRange.end();
            }
        }
        // Add the last meeting time if possible. 
        if(TimeRange.END_OF_DAY - startTime >= minimumDuration) {
            meetingTimes.add(TimeRange.fromStartEnd(startTime, TimeRange.END_OF_DAY, true));
        }
        return meetingTimes;
    }
}



