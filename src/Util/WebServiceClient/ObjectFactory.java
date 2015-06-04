
package Util.WebServiceClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the Util.WebServiceClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetBookDetailJson_QNAME = new QName("http://WebService.Util/", "getBookDetailJson");
    private final static QName _GetBookDetailJsonResponse_QNAME = new QName("http://WebService.Util/", "getBookDetailJsonResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: Util.WebServiceClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetBookDetailJsonResponse }
     * 
     */
    public GetBookDetailJsonResponse createGetBookDetailJsonResponse() {
        return new GetBookDetailJsonResponse();
    }

    /**
     * Create an instance of {@link GetBookDetailJson }
     * 
     */
    public GetBookDetailJson createGetBookDetailJson() {
        return new GetBookDetailJson();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBookDetailJson }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.Util/", name = "getBookDetailJson")
    public JAXBElement<GetBookDetailJson> createGetBookDetailJson(GetBookDetailJson value) {
        return new JAXBElement<GetBookDetailJson>(_GetBookDetailJson_QNAME, GetBookDetailJson.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetBookDetailJsonResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://WebService.Util/", name = "getBookDetailJsonResponse")
    public JAXBElement<GetBookDetailJsonResponse> createGetBookDetailJsonResponse(GetBookDetailJsonResponse value) {
        return new JAXBElement<GetBookDetailJsonResponse>(_GetBookDetailJsonResponse_QNAME, GetBookDetailJsonResponse.class, null, value);
    }

}
