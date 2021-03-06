package com.epam.cdp.module4.hw2.controller;

import com.epam.cdp.module4.hw2.exceptions.EventNotFoundException;
import com.epam.cdp.module4.hw2.facade.impl.BookingFacadeImpl;
import com.epam.cdp.module4.hw2.model.Event;
import com.epam.cdp.module4.hw2.model.impl.EventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/events")
public class EventController {

    private static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private BookingFacadeImpl bookingFacade;

    /**
     * Get event by id
     *
     * @param id    event id
     * @param model model
     * @return model view
     */
    @GetMapping(value = "/{id}")
    public String getEventById(@PathVariable(value = "id") int id, Model model) {
        Event event = bookingFacade.getEventById(id);
        if (event == null) {
            throw new EventNotFoundException(id);
        }
        model.addAttribute("events", Collections.singleton(event));
        return "events";
    }

    /**
     * Get events by title
     *
     * @param eventTitle event title
     * @param pageSize   page size
     * @param page       page number
     * @param model      model
     * @return model view
     */
    @GetMapping(params = {"eventTitle", "pageSize", "page"})
    public String getEventsByTitle(
            @RequestParam(value = "eventTitle") String eventTitle,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "page") int page,
            Model model) {
        List<Event> events = bookingFacade.getEventsByTitle(eventTitle, pageSize, page);
        model.addAttribute("events", events);
        return "events";
    }

    /**
     * Get events by date
     *
     * @param eventDate event date
     * @param pageSize  page size
     * @param page      page number
     * @param model     model
     * @return model view
     */
    @GetMapping(params = {"eventDate", "pageSize", "page"})
    public String getEventByDate(
            @RequestParam(value = "eventDate") String eventDate,
            @RequestParam(value = "pageSize") int pageSize,
            @RequestParam(value = "page") int page,
            Model model) {
        List<Event> events = bookingFacade.getEventsForDay(Date.valueOf(eventDate), pageSize, page);
        model.addAttribute("events", events);
        return "events";
    }

    /**
     * Create event
     *
     * @param title event name
     * @param date  event date
     * @param model model
     * @return model view
     */
    @GetMapping(value = "/create", params = {"title", "date"})
    public String createEvent(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "date") String date,
            Model model) {
        Event event = new EventImpl();
        event.setTitle(title);
        event.setDate(new java.sql.Date(Date.valueOf(date).getTime()).toLocalDate());

        Event result = bookingFacade.createEvent(event);
        model.addAttribute("events", Collections.singleton(result));
        return "events";
    }

    /**
     * Update event
     *
     * @param id    event id
     * @param title event title
     * @param date  event date
     * @param model model
     * @return model view
     */
    @GetMapping(value = "/update", params = {"id", "title", "date"})
    public String updateEvent(
            @RequestParam(value = "id") int id,
            @RequestParam(value = "title") String title,
            @RequestParam(value = "date") String date,
            Model model) {
        Event event = new EventImpl();
        event.setId(id);
        event.setTitle(title);
        event.setDate(new java.sql.Date(Date.valueOf(date).getTime()).toLocalDate());

        Event result = bookingFacade.updateEvent(event);
        model.addAttribute("events", Collections.singleton(result));
        return "events";
    }

    /**
     * Delete event by id
     *
     * @param id    event id
     * @param model model
     * @return model view
     */
    @GetMapping(value = "/delete", params = {"id"})
    public String deleteEvent(@RequestParam(value = "id") int id, Model model) {
        boolean result = bookingFacade.deleteEvent(id);
        if (!result) {
            throw new EventNotFoundException(id);
        }
        model.addAttribute("deleted", result);
        return "events";
    }

}
