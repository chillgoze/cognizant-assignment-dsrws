package assignment.dsrws;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController("/storage")
public class DocumentStorageOperations {

    Map<String, String> storage;   
    int idCounter;

    public DocumentStorageOperations() {

        this.storage = new HashMap<>();
        this.idCounter = 0;
    }

    protected Map<String, String> getStorage() {

        return storage;
    }

    /**
     * Create a document.
     * 
     * @param Document Content
     * @return Document Id
     */
    @PostMapping(path = "/documents", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    protected String create(@RequestBody String document) {

        String id = null;
        synchronized (this) {
            id = String.format("%s", idCounter++);
            storage.put(id, document);
        }
        System.out.println(document);
        return id;
    }


    /**
     * Update a document.
     * 
     * @param Document Id
     * @param Document Content
     */
    @PutMapping(path = "/documents/{docId}", consumes = MediaType.ALL_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    protected void update(@PathVariable("docId") String id, @RequestBody String document) {

        if (getStorage().containsKey(id)) {
            getStorage().put(id, document);
        } else {
            // Not Found
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    /**
     * Get a previously stored document.
     * 
     * @param Document Id
     * @return Document Content
     */
    @GetMapping("/documents/{docId}")
    @ResponseStatus(HttpStatus.FOUND)
    protected String query(@PathVariable("docId") String id) {

        String document = getStorage().get(id);

        // Not Found
        if (document == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return document;
    }


    /**
     * Delete a document.
     * 
     * @param Document Id
     */
    @DeleteMapping("/documents/{docId}")
    protected void delete(@PathVariable("docId") String id) {

        String document = getStorage().remove(id);
        if (document == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Ping
     * 
     * @return Current Timestamp
     */
    @GetMapping("/ping")
    @ResponseStatus(HttpStatus.OK)
    protected String ping() {
        return Calendar.getInstance().getTime().toString();
    }

}