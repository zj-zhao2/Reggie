package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dao.AddresssBookDao;
import com.domain.AddressBook;
import com.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddresssBookDao, AddressBook> implements AddressBookService {
}
