package com.conferences.controller;

import com.conferences.domain.Conference;
import com.conferences.domain.UserConference;
import com.conferences.model.ReportStatus;
import com.conferences.model.UserConferenceRole;
import com.conferences.service.ConferenceService;
import com.conferences.service.NoConferenceExistedForTheUserException;
import com.conferences.service.UserConferenceService;
import com.conferences.service.UserService;
import com.conferences.service.dto.ConferenceDTO;
import com.conferences.service.dto.UserConferenceDTO;
import com.conferences.service.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
public class ConferenceController {
    private final ConferenceService conferenceService;
    private final UserService userService;
    private final UserConferenceService userConferenceService;
    private final int ROW_PER_PAGE = 5;
    @Value("${msg.conferences.title}")
    private String conferencesTitle;
    @Value("${msg.conferencesshow.title}")
    private String conferencesInfoTitle;
    @Value("${msg.conferencesshow.title}")
    private String conferencesAddTitle;
    @Value("${msg.conferencesedit.title}")
    private String conferencesEditTitle;
    @Value("${msg.myconferences.title}")
    private String conferencesListTitle;
    @Value("${msg.requests.title}")
    private String requestsTitle;
    @Value("${msg.participants.title}")
    private String participantsTitle;
    @Autowired
    private ServletContext servletContext;
    
    public ConferenceController(ConferenceService conferenceService,
                                UserService userService,
                                UserConferenceService userConferenceService) {
        this.conferenceService = conferenceService;
        this.userService = userService;
        this.userConferenceService = userConferenceService;
    }
    
    @GetMapping(value = "/{userId}/allconferences")
    public String showAllConferences(Model model,
                                     @RequestParam(value = "page", defaultValue = "1") int pageNum,
                                     @PathVariable String userId) {
        model.addAttribute("title",
                           conferencesTitle
                          );
        List<ConferenceDTO> allConferences = conferenceService.getAllExistingConferences(pageNum,
                                                                                         ROW_PER_PAGE
                                                                                        );
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        if (allConferences.size() != 0) {
            long count = conferenceService.count();
            boolean hasPrev = pageNum > 1;
            boolean hasNext = (pageNum * ROW_PER_PAGE) < count;
            model.addAttribute("conferences",
                               allConferences
                              );
            model.addAttribute("title",
                               "All Conferences Page"
                              );
            model.addAttribute("hasPrev",
                               hasPrev
                              );
            model.addAttribute("hasNext",
                               hasNext
                              );
            model.addAttribute("prev",
                               pageNum - 1
                              );
            model.addAttribute("next",
                               pageNum + 1
                              );
            model.addAttribute("user",
                               user
                              );
        }
        else {
            model.addAttribute("errorMessage",
                               "No Conferences Yet"
                              );
        }
        return "conferences";
    }
    
    @GetMapping(value = "/{userId}/conferences/{conferenceId}/show")
    public String showConferenceInfo(Model model,
                                     @PathVariable String conferenceId,
                                     @PathVariable String userId) {
        ConferenceDTO conference = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        Optional<UserConference> userConference = userConferenceService.getUserConferenceRelation(user,
                                                                                                  conference
                                                                                                 );
        model.addAttribute("title",
                           conferencesInfoTitle
                          );
        model.addAttribute("conference",
                           conference
                          );
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("userconf",
                           userConference.orElseGet(UserConference::new)
                          );
        
        return "conference-info";
    }
    
    @PostMapping(value = "/{userId}/conferences/{conferenceId}/{action}/{role}")
    public String linkUnlink(Model model,
                             @PathVariable String userId,
                             @PathVariable String conferenceId,
                             @PathVariable String action,
                             @PathVariable String role) {
        if (action.equals("delete")) {
            userConferenceService.deleteRelation(userId,
                                                 conferenceId
                                                );
        }
        else {
            log.debug("ROLE IS: " + role);
            UserConferenceDTO userConferenceDTO = new UserConferenceDTO(userId,
                                                                        conferenceId,
                                                                        role.equals("SPEAKER") ?
                                                                        UserConferenceRole.SPEAKER :
                                                                        UserConferenceRole.ATTENDEE
            );
            userConferenceService.create(userConferenceDTO);
        }
        ConferenceDTO conference = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        model.addAttribute("conference",
                           conference
                          );
        model.addAttribute("user",
                           user
                          );
        return "redirect:/" + String.format("%s/conferences/%s/show",
                                            userId,
                                            conferenceId
                                           );
    }
    
    @GetMapping(value = "{userId}/conferences/add")
    public String showCreateConferencePage(Model model,
                                           @PathVariable String userId) {
        model.addAttribute("title",
                           conferencesAddTitle
                          );
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        ConferenceDTO conferenceDTO = new ConferenceDTO();
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("conference",
                           conferenceDTO
                          );
        return "add-conference";
    }
    
    @PostMapping(value = "/{userId}/conferences/add")
    public String addConference(Model model,
                                @PathVariable String userId,
                                @ModelAttribute("conference") ConferenceDTO conference,
                                RedirectAttributes redirectAttributes) {
        if (conference.getTitle().isEmpty() ||
            conference.getShortDescription().isEmpty() ||
            conference.getLocation().isEmpty() ||
            conference.getOrganiser().isEmpty() ||
            conference.getContacts().isEmpty() ||
            conference.getStartDate() == null ||
            conference.getEndDate() == null ||
            conference.getRegistrationStartDate() == null ||
            conference.getRegistrationEndDate() == null
        ) {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 "Mandatory Fields must not be empty"
                                                );
            return "redirect:/" + String.format("%s/conferences/add",
                                                userId
                                               );
        }
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        conference.setCreatedBy(user.getId());
        try {
            Conference createdConference = conferenceService.createConference(conference
                                                                             );
            model.addAttribute("user",
                               user
                              );
            model.addAttribute("conference",
                               createdConference
                              );
            return String.format("redirect:/%s/conferences/%s/show",
                                 user.getId(),
                                 createdConference.getConferenceId()
                                );
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 e.getMessage()
                                                );
            return "redirect:/" + String.format("%s/conferences/add",
                                                userId
                                               );
        }
    }
    
    @GetMapping(value = "{userId}/conferences/{conferenceId}/edit")
    public String showEditConferencePage(@PathVariable String userId,
                                         @PathVariable String conferenceId,
                                         Model model) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        ConferenceDTO conferenceDTO = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("title",
                           conferencesEditTitle
                          );
        model.addAttribute("conference",
                           conferenceDTO
                          );
        return "update-conference";
    }
    
    @PostMapping(value = "/{userId}/conferences/{conferenceId}/edit")
    public String updateConferenceInfo(Model model,
                                       @PathVariable String userId,
                                       @PathVariable String conferenceId,
                                       @ModelAttribute("conference") ConferenceDTO conference) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        conference.setId(Long.parseLong(conferenceId));
        Optional<ConferenceDTO> updatedConference = conferenceService.updateConference(conference);
        model.addAttribute("conference",
                           updatedConference
                          );
        model.addAttribute("user",
                           user
                          );
        return String.format("redirect:/%s/conferences/%s/show",
                             user.getId(),
                             updatedConference.get().getId()
                            );
    }
    
    @GetMapping(value = "/{userId}/myrequests")
    public String showMyRequests(Model model,
                                 @RequestParam(value = "page", defaultValue = "1") int pageNum,
                                 @PathVariable String userId) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        model.addAttribute("title",
                           requestsTitle
                          );
        model.addAttribute("user",
                           user
                          );
        try {
            List<ConferenceDTO> myConferences = userService.getUserConferences(userId,
                                                                               pageNum,
                                                                               ROW_PER_PAGE
                                                                              );
            
            long count = myConferences.size();
            boolean hasPrev = pageNum > 1;
            boolean hasNext = (pageNum * ROW_PER_PAGE) < count;
            model.addAttribute("conferences",
                               myConferences
                              );
            model.addAttribute("hasPrev",
                               hasPrev
                              );
            model.addAttribute("hasNext",
                               hasNext
                              );
            model.addAttribute("prev",
                               pageNum - 1
                              );
            model.addAttribute("next",
                               pageNum + 1
                              );
            
        }
        catch (NoConferenceExistedForTheUserException e) {
            model.addAttribute("errorMessage",
                               e.getMessage()
                              );
        }
        
        return "my-requests";
    }
    
    @GetMapping(value = "/{userId}/myconferences")
    public String showMyConferences(Model model,
                                    @RequestParam(value = "page", defaultValue = "1") int pageNum,
                                    @PathVariable String userId) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        model.addAttribute("title",
                           conferencesTitle
                          );
        model.addAttribute("user",
                           user
                          );
        List<ConferenceDTO> myConferences = conferenceService.getUserCreatedConferences(user,
                                                                                        1,
                                                                                        5
                                                                                       );
        
        model.addAttribute("title",
                           conferencesListTitle
                          );
        model.addAttribute("conferences",
                           myConferences
                          );
        model.addAttribute("prev",
                           pageNum - 1
                          );
        model.addAttribute("next",
                           pageNum + 1
                          );
        if (myConferences.size() == 0) {
            model.addAttribute("errorMessage",
                               "No Created Conferences"
                              );
        }
        
        return "my-conferences";
    }
    
    @PostMapping(value = "/{userId}/conferences/{conferenceId}/delete")
    public String deleteConference(Model model,
                                   @PathVariable String userId,
                                   @PathVariable String conferenceId) {
        
        conferenceService.deleteConference(conferenceId);
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        model.addAttribute("user",
                           user
                          );
        return "redirect:/" + String.format("%s/myconferences",
                                            userId
                                           );
    }
    
    @PostMapping(value = "/{userId}/conferences/{conferenceId}/uploadreport")
    public String uploadReportFile(Model model,
                                   @PathVariable String userId,
                                   @PathVariable String conferenceId,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String reportPath = "/Users/andreigavrilov/Work/Projects/Diploma/Conferences/src/main/resources/files/reports/";
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        ConferenceDTO conferenceDTO = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("conference",
                           conferenceDTO
                          );
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 "Please select a file to upload"
                                                );
            
            return String.format("redirect:/%s/conferences/%s/show",
                                 user.getId(),
                                 conferenceDTO.getId()
                                );
        }
        
        try {
            //Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(reportPath + file.getOriginalFilename());
            Files.write(path,
                        bytes
                       );
            Optional<UserConference> userConference = userConferenceService.getUserConferenceRelation(user,
                                                                                                      conferenceDTO
                                                                                                     );
            UserConferenceDTO userConferenceDTO = new UserConferenceDTO(userConference.get());
            userConferenceDTO.setReportPath(reportPath + file.getOriginalFilename());
            userConferenceDTO.setReportStatus(ReportStatus.UNDER_REVIEW);
            userConferenceService.updateRelation(userConferenceDTO,
                                                 user,
                                                 conferenceDTO
                                                );
            
            model.addAttribute("message",
                               "You successfully uploaded '" + file.getOriginalFilename() + "'"
                              );
            model.addAttribute("userconf",
                               userConferenceDTO
                              );
            
        }
        catch (IOException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage",
                               e.getMessage()
                              );
        }
        
        return String.format("redirect:/%s/conferences/%s/show",
                             user.getId(),
                             conferenceDTO.getId()
                            );
    }
    
    @PostMapping(value = "/{userId}/conferences/{conferenceId}/uploadreview")
    public String uploadReviewFile(Model model,
                                   @PathVariable String userId,
                                   @PathVariable String conferenceId,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        String reviewPath = "/Users/andreigavrilov/Work/Projects/Diploma/Conferences/src/main/resources/files/reviews/";
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        ConferenceDTO conferenceDTO = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("conference",
                           conferenceDTO
                          );
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage",
                                                 "Please select a file to upload"
                                                );
            
            return String.format("redirect:/%s/conferences/%s/participants",
                                 user.getId(),
                                 conferenceDTO.getId()
                                );
        }
        
        try {
            //Get the file and save it somewhere
            byte[] bytes = file.getBytes();
            Path path = Paths.get(reviewPath + file.getOriginalFilename());
            Files.write(path,
                        bytes
                       );
            Optional<UserConference> userConference = userConferenceService.getUserConferenceRelation(user,
                                                                                                      conferenceDTO
                                                                                                     );
            UserConferenceDTO userConferenceDTO = new UserConferenceDTO(userConference.get());
            userConferenceDTO.setReviewPath(reviewPath + file.getOriginalFilename());
            userConferenceDTO.setReportStatus(ReportStatus.ACCEPTED);
            userConferenceService.updateRelation(userConferenceDTO,
                                                 user,
                                                 conferenceDTO
                                                );
            
            model.addAttribute("message",
                               "You successfully uploaded '" + file.getOriginalFilename() + "'"
                              );
            model.addAttribute("userconf",
                               userConferenceDTO
                              );
            
        }
        catch (IOException e) {
            log.error(e.getMessage());
            model.addAttribute("errorMessage",
                               e.getMessage()
                              );
        }
        
        return String.format("redirect:/%s/conferences/%s/participants",
                             user.getId(),
                             conferenceDTO.getId()
                            );
    }
    
    @PostMapping(value = "/{userId}/conferences/{conferenceId}/participants/{action}")
    public String approveOrRejectReport(Model model,
                                        @PathVariable String userId,
                                        @PathVariable String conferenceId,
                                        @PathVariable String action) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        ConferenceDTO conferenceDTO = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("conference",
                           conferenceDTO
                          );
        
        Optional<UserConference> userConference = userConferenceService.getUserConferenceRelation(user,
                                                                                                  conferenceDTO
                                                                                                 );
        UserConferenceDTO userConferenceDTO = new UserConferenceDTO(userConference.get());
        userConferenceDTO.setReportStatus(action.equals("Approve") ? ReportStatus.ACCEPTED : ReportStatus.REJECTED);
        userConferenceService.updateRelation(userConferenceDTO,
                                             user,
                                             conferenceDTO
                                            );
        model.addAttribute("userconf",
                           userConferenceDTO
                          );
        
        return String.format("redirect:/%s/conferences/%s/participants",
                             user.getId(),
                             conferenceDTO.getId()
                            );
    }
    
    @GetMapping("/download")
    public void downloadReportFile(HttpServletResponse resonse,
                                   @RequestParam String filePath) throws IOException {
        String directory = filePath;
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        MediaType mediaType = MediaTypeUtils.getMediaTypeForFileName(this.servletContext,
                                                                     fileName
                                                                    );
        System.out.println("filePath: " + filePath);
        System.out.println("fileName: " + fileName);
        System.out.println("mediaType: " + mediaType);
        
        File file = new File(filePath);
        // Content-Type
        // application/pdf
        resonse.setContentType(mediaType.getType());
        
        // Content-Disposition
        resonse.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                          "attachment;filename=" + file.getName()
                         );
        // Content-Length
        resonse.setContentLength((int) file.length());
        
        BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(file));
        BufferedOutputStream outStream = new BufferedOutputStream(resonse.getOutputStream());
        
        byte[] buffer = new byte[1024];
        int bytesRead = 0;
        while ((bytesRead = inStream.read(buffer)) != -1) {
            outStream.write(buffer,
                            0,
                            bytesRead
                           );
        }
        outStream.flush();
        inStream.close();
    }
    
    @GetMapping(value = "/{userId}/conferences/{conferenceId}/participants")
    public String getParticipantsInfo(Model model,
                                      @PathVariable String userId,
                                      @PathVariable String conferenceId) {
        UserDTO user = new UserDTO(userService.findById(Long.parseLong(userId)));
        ConferenceDTO conferenceDTO = new ConferenceDTO(conferenceService.findById(Long.parseLong(conferenceId)));
        List<UserDTO> participants = userService.getConferenceParticipants(conferenceDTO);
        model.addAttribute("title",
                           participantsTitle
                          );
        model.addAttribute("user",
                           user
                          );
        model.addAttribute("conference",
                           conferenceDTO
                          );
        if (participants.size() != 0) {
            List<UserConferenceDTO> userConferences = new ArrayList<>();
            participants.forEach(u -> userConferences.add(new UserConferenceDTO(userConferenceService
                                                                                      .getUserConferenceRelation(u,
                                                                                                                 conferenceDTO
                                                                                                                )
                                                                                      .get())));
            model.addAttribute("participants",
                               participants
                              );
            model.addAttribute("usersconf",
                               userConferences
                              );
        }
        else {
            model.addAttribute("errorMessage",
                               "No Participants for this Conference"
                              );
        }
        return "conference-participants";
    }
}
