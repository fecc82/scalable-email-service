package com.email.service.impl;

import com.email.service.api.EmailService;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
@Service
@Qualifier("mailGun")
public class MailGunServiceImpl implements EmailService {
    private final String MAIL_GUN_API = "key-dbcd44fde4f708379d67150fe0c75d4d";
    private final String API = "https://api.mailgun.net/v3/sandbox9073ddbcd8d14680ad6289ef9ca73ed2.mailgun.org/messages";
    public void send(String to, String cc, String email) {
        System.out.println("Attempting to send email via " + API);
        try {
            Client client = Client.create();
            client.addFilter(new HTTPBasicAuthFilter("api",MAIL_GUN_API));
            WebResource webResource = client.resource(API);
            MultivaluedMapImpl formData = new MultivaluedMapImpl();
            formData.add("from", "meltatlonghari3@gmail.com");
            formData.add("to", "meltatlonghari3@gmail.com");
            formData.add("subject", "Hello 2");
            formData.add("text", "Testing some Mailgun awesomeness!");
            Object o = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                    post(ClientResponse.class, formData);
            System.out.println(o);
            System.out.println("BREAK");

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
