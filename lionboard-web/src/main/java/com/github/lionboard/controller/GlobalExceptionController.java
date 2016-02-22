package com.github.lionboard.controller;

import com.github.lionboard.error.IncorrectAccessException;
import com.github.lionboard.error.InvalidCmtException;
import com.github.lionboard.error.InvalidPostException;
import com.github.lionboard.error.InvalidUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Lion.k on 16. 2. 18..
 */

@ControllerAdvice
public class GlobalExceptionController {


    private static final Logger logger =
            LoggerFactory.getLogger(FileController.class);

    @ExceptionHandler(InvalidPostException.class)
    public ModelAndView catchInvalidException(InvalidPostException e) {
        logger.debug(e.getMessage());
        return new ModelAndView("/errors/failure").addObject("errorlog", e.getMessage());
    }

    @ExceptionHandler(IncorrectAccessException.class)
    public ModelAndView catchIncorrectAccessException(IncorrectAccessException e) {
        logger.debug(e.getMessage());
        return new ModelAndView("/errors/denied").addObject("errorlog", e.getMessage());
    }


    @ExceptionHandler(InvalidCmtException.class)
    public ModelAndView catchInvalidException(InvalidCmtException e) {
        logger.debug(e.getMessage());
        return new ModelAndView("/errors/failure").addObject("errorlog", e.getMessage());
    }


    @ExceptionHandler(RuntimeException.class)
    public ModelAndView catchRuntimeException(RuntimeException e) {
        logger.debug(e.getMessage());
        return new ModelAndView("/errors/failure").addObject("errorlog", "파라미터 값이 올바르지 않습니다.");
    }

    @ExceptionHandler(InvalidUserException.class)
    public ModelAndView catchInvalidException(InvalidUserException e) {
        logger.debug(e.getMessage());
        return new ModelAndView("/errors/failure").addObject("errorlog", e.getMessage());
    }

}
