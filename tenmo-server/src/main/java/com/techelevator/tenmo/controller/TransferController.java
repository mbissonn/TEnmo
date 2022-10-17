package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
@RestController
public class TransferController {

    private TransferDao transferDao;
    private UserDao userDao;


    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getTransferHistory(Principal principal) {
        return transferDao.getTransferHistory(userDao.findIdByUsername(principal.getName()));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping( path = "/transfers", method = RequestMethod.POST)
    public Transfer addTransfer(@RequestBody Transfer transfer) {
         transferDao.createSend(transfer);
         return transfer;
    }
}
