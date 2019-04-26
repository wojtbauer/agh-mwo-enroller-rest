package com.company.enroller.controllers;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingRestController {

	@Autowired
	MeetingService meetingService;

	@Autowired
	ParticipantService participantService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> meetings = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(meetings, HttpStatus.OK);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
	     Meeting meeting = meetingService.findById(id);
	     if (meeting == null) {
	         return new ResponseEntity(HttpStatus.NOT_FOUND);
	     }
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.OK);
	 }

	 @RequestMapping(value = "", method = RequestMethod.POST)
	 public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
		 Meeting foundMeeting = meetingService.findById(meeting.getId());
	     if (foundMeeting != null) {
	    	 return new ResponseEntity("Unable to create. A meeting with id " + meeting.getId() + " already exist.", HttpStatus.CONFLICT);
	     }
	     meetingService.addMeeting(meeting);
	     return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getParticipants(@PathVariable("id") long id) {
		Collection<Participant> participants = meetingService.getParticipants(id);
		return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
	}

	 @RequestMapping(value = "/{id}/participants", method = RequestMethod.POST)
	 public ResponseEntity<?> registerParticipant(@PathVariable("id") long id,
				@RequestBody Participant participant) {
		 Participant foundParticipant = participantService.findByLogin(participant.getLogin());
	     if (foundParticipant != null) {
	    	 return new ResponseEntity("Unable to create. A participant with id " + participant.getLogin() + " already exist.", HttpStatus.CONFLICT);
	     }
	     meetingService.addParticipant(id, participant);
	     return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	 }

}