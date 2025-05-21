package com.itmo.nxzage.server.services.storage;

import com.itmo.nxzage.common.util.data.Person;
import com.itmo.nxzage.server.Storage;

/**
 * Фасад для работы со Storage с объектами типа Person
 */
public class PersonStorageServices {
    BaseStorageService<Person> baseService; 
    PersonStorageService personService;

    public PersonStorageServices(Storage<Person> storage) {
        baseService = new BaseStorageService<Person>(storage);
        personService = new PersonStorageService(storage);
    }

    public BaseStorageService<Person> baseService() {
        return baseService;
    }
    public PersonStorageService personService() {
        return personService;
    }
    
}
