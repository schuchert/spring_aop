package shoe.example.endpoint;

import org.springframework.stereotype.Service;
import shoe.example.service.SomeService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.inject.Inject;

@Service
@Path("/somerestendpoints")
public class SomeRestEndpoints {
    @Inject
    SomeService service;

    @GET
    @Path("/method1")
    public String method1() {
        service.method1();
        return "success";
    }

    @GET
    @Path("/method2")
    public String method2() {
        service.methodThrowingException();
        return "success";
    }
}
