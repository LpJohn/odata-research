package com.lampsplus.odata_research.client.odataclient;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.olingo.client.api.communication.request.retrieve.EdmMetadataRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataServiceDocumentRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ODataEntitySetIterator;
// import org.apache.olingo.client.api.edm.xml.v4.annotation.Collection;
import org.apache.olingo.client.api.v4.ODataClient;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.commons.api.domain.ODataServiceDocument;
import org.apache.olingo.commons.api.domain.v4.ODataEntity;
import org.apache.olingo.commons.api.domain.v4.ODataEntitySet;
import org.apache.olingo.commons.api.domain.v4.ODataProperty;
import org.apache.olingo.commons.api.domain.v4.ODataValue;
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
	private final static String _serviceRoot = "http://localhost:62390/odata";
    public static void main( String[] args )
    {
        System.out.println( "Hello OData!" );
        
        showMetaData();
        showData();
    }
    
    private static void showMetaData() {

        ODataClient client = ODataClientFactory.getV4();

        ODataServiceDocumentRequest req = client.getRetrieveRequestFactory().getServiceDocumentRequest(_serviceRoot);
        ODataRetrieveResponse<ODataServiceDocument> res = req.execute();

        ODataServiceDocument serviceDocument = res.getBody();

        Collection<String> entitySetNames = serviceDocument.getEntitySetNames();
        Map<String,URI> entitySets = serviceDocument.getEntitySets();
        Map<String,URI> singletons = serviceDocument.getSingletons();
        Map<String,URI> functionImports = serviceDocument.getFunctionImports();
        URI productsUri = serviceDocument.getEntitySetURI("Products");
        
        EdmMetadataRequest request = client.getRetrieveRequestFactory().getMetadataRequest(_serviceRoot);
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
    
    private static void showData() {
        ODataClient client = ODataClientFactory.getV4();
        URI productsUri = client.newURIBuilder(_serviceRoot).appendEntitySetSegment("Products").build();   	
        
        ODataRetrieveResponse<ODataEntitySetIterator<ODataEntitySet, ODataEntity>> response = 
        		client.getRetrieveRequestFactory().getEntitySetIteratorRequest(productsUri).execute();

        ODataEntitySetIterator<ODataEntitySet, ODataEntity> iterator = response.getBody();

        while (iterator.hasNext()) {
            ODataEntity customer = iterator.next();
            List<ODataProperty> properties = customer.getProperties();
            for (ODataProperty property : properties) {
                String name = property.getName();
                ODataValue value = property.getValue();
                String valueType = value.getTypeName();
                System.out.println("Name: "+name+"\tValue: "+value + "+\tType:" + valueType);
            }
        }
    }
    
}
