package pl.kielce.tu.przedszkole.przedszkole.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.kielce.tu.przedszkole.przedszkole.dto.ClassActionDto;
import pl.kielce.tu.przedszkole.przedszkole.dto.LoginData;
import pl.kielce.tu.przedszkole.przedszkole.dto.Message;
import pl.kielce.tu.przedszkole.przedszkole.security.custom.CustomLoginUtility;
import pl.kielce.tu.przedszkole.przedszkole.service.ClassProxyDispatcher;

import javax.naming.AuthenticationException;

@RestController
@CrossOrigin
@RequestMapping("/class")
public class ClassController {
    private final CustomLoginUtility customLoginUtility;
    private final ClassProxyDispatcher classProxyDispatcher;

    @Autowired
    public ClassController(CustomLoginUtility customLoginUtility,
                           ClassProxyDispatcher classProxyDispatcher) {
        this.customLoginUtility = customLoginUtility;
        this.classProxyDispatcher= classProxyDispatcher;
    }

    @CrossOrigin
    @RequestMapping(value="/add", method = RequestMethod.POST)
    ResponseEntity<?> addChild(@RequestBody ClassActionDto classActionDto) {
        Message message;
        try {
            customLoginUtility.validateAuthentication(classActionDto.getLoginData());
            classProxyDispatcher.addClass(classActionDto.getLoginData().getLogin(), classActionDto.getTransferedClassroom());
            message = new Message(200, "Classroom added.");
            return ResponseEntity.ok(message);
        } catch(AuthenticationException e){
            message = new Message(401, "Invalid auth data");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin
    @RequestMapping(value="/edit", method = RequestMethod.POST)
    ResponseEntity<?> editChild(@RequestBody ClassActionDto classActionDto) {
        Message message;
        try {
            customLoginUtility.validateAuthentication(classActionDto.getLoginData());
            classProxyDispatcher.editClass(classActionDto.getLoginData().getLogin(), classActionDto.getTransferedClassroom());
            message = new Message(200, "Classroom edited.");
            return ResponseEntity.ok(message);
        } catch(AuthenticationException e){
            message = new Message(401, "Invalid auth data");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        } catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    ResponseEntity<?> deleteChild(@RequestBody ClassActionDto classActionDto) {
        Message message;
        try {
            customLoginUtility.validateAuthentication(classActionDto.getLoginData());
            classProxyDispatcher.deleteClass(classActionDto.getLoginData().getLogin(), classActionDto.getClassId());
            message = new Message(200, "Classroom deleted.");
            return ResponseEntity.ok(message);
        } catch(AuthenticationException e) {
            message = new Message(401, "Invalid auth data");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/list", method=RequestMethod.POST)
    ResponseEntity<?> getChildren(@RequestBody LoginData loginData) {
        Message message;
        try {
            customLoginUtility.validateAuthentication(loginData);
            return ResponseEntity.ok(classProxyDispatcher.getClasses(loginData.getLogin()));
        } catch(AuthenticationException e) {
            message = new Message(401, "Invalid auth data");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
        catch(Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @CrossOrigin
    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    ResponseEntity<?> getChildById(@RequestBody ClassActionDto classActionDto) {
        Message message;
        try {
            customLoginUtility.validateAuthentication(classActionDto.getLoginData());
            return ResponseEntity.ok(classProxyDispatcher.getClassById(classActionDto.getLoginData().getLogin()
                    ,classActionDto.getClassId()));
        } catch(AuthenticationException e) {
            message = new Message(401, "Invalid auth data");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(message);
        }
    }
}
