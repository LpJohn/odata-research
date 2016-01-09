package com.lampsplus.odata_research.client.odataclient;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.olingo.client.api.communication.request.retrieve.EdmMetadataRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataServiceDocumentRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
// import org.apache.olingo.client.api.edm.xml.v4.annotation.Collection;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.domain.ODataServiceDocument;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmComplexType;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmSchema;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );

        ODataClient client = ODataClientFactory.getV4();

        String serviceRoot = "http://localhost:62390/odata";
        ODataServiceDocumentRequest req = client.getRetrieveRequestFactory().getServiceDocumentRequest(serviceRoot);
        ODataRetrieveResponse<ODataServiceDocument> res = req.execute();

        ODataServiceDocument serviceDocument = res.getBody();

        Collection<String> entitySetNames = serviceDocument.getEntitySetNames();
        Map<String,URI> entitySets = serviceDocument.getEntitySets();
        Map<String,URI> singletons = serviceDocument.getSingletons();
        Map<String,URI> functionImports = serviceDocument.getFunctionImports();
        URI productsUri = serviceDocument.getEntitySetURI("Products");
        
        EdmMetadataRequest request = client.getRetrieveRequestFactory().getMetadataRequest(serviceRoot);
        ODataRetrieveResponse<Edm> response = request.execute();

        Edm edm = response.getBody();
        System.out.println(edm);

        List<EdmSchema> schemas = edm.getSchemas();
        for (EdmSchema schema : schemas) {
            String namespace = schema.getNamespace();
            for (EdmComplexType complexType : schema.getComplexTypes()) {
                FullQualifiedName name = complexType.getFullQualifiedName();
                System.out.println(name);
            }
            for (EdmEntityType entityType : schema.getEntityTypes()) {
                FullQualifiedName name = entityType.getFullQualifiedName();
                System.out.println(name);
            }
        }

        EdmEntityType customerType = edm.getEntityType(new FullQualifiedName("ProductService.Models", "Product"));
        List<String> propertyNames = customerType.getPropertyNames();
        for (String propertyName : propertyNames) {
            EdmProperty property = customerType.getStructuralProperty(propertyName);
            FullQualifiedName typeName = property.getType().getFullQualifiedName();
            System.out.println(typeName);
        }
    }
}
