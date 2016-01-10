package com.lampsplus.odata_research.client.odataclient;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.olingo.client.api.communication.request.retrieve.EdmMetadataRequest;
import org.apache.olingo.client.api.communication.request.retrieve.ODataRetrieveRequest;
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
 * Based on code from https://templth.wordpress.com/2014/12/03/accessing-odata-v4-service-with-olingo/
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
        showData(client, productsUri);
        
        productsUri = client.newURIBuilder(_serviceRoot).appendEntitySetSegment("Products").select("Name,Price").build();       
        showData(client, productsUri);
        
        productsUri = client.newURIBuilder(_serviceRoot).appendEntitySetSegment("Products").filter("Category eq 'Lighting' and Price ge 100").build();
        showData(client, productsUri);
    }
    
    private static void showData(ODataClient client, URI productsUri ) {

    	System.out.println(productsUri);
    	
        ODataRetrieveRequest<ODataEntitySetIterator<ODataEntitySet, ODataEntity>> request = 
        		client.getRetrieveRequestFactory().getEntitySetIteratorRequest(productsUri);
    	
        request.addCustomHeader("env", "test"); // set custom header so server knows which database to use

        ODataRetrieveResponse<ODataEntitySetIterator<ODataEntitySet, ODataEntity>> response = request.execute();

        ODataEntitySetIterator<ODataEntitySet, ODataEntity> iterator = response.getBody();
        
        boolean first = true;
        StringBuilder title = new StringBuilder();
        StringBuilder data = new StringBuilder();
        while (iterator.hasNext()) {
            ODataEntity product = iterator.next();
            List<ODataProperty> properties = product.getProperties();
            for (ODataProperty property : properties) {
                String name = property.getName();
                ODataValue value = property.getValue();
                String valueType = value.getTypeName();
                if (first)
                        title.append(name + " (" + valueType + ")\t");
                data.append(value + "\t");
            }
            if (first) {
            	title.append("\r\n");
            	first = false;
            }
            data.append("\r\n");
        }
        
        System.out.println(title + "\r\n" + data);
    }
    
}
