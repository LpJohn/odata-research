﻿using ProductService.Models;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Threading.Tasks;
using System.Web.Http;
using System.Web.OData;
using System.Web.OData.Routing;

namespace ProductService.Controllers
{
    public class ProductsController : ODataController
    {
        public IQueryable<Product> Products
        {
            get
            {
                return (new List<Product> 
                    {
                        new Product { Id = 1, Name = "Lamp", Category = "Lighting", Price = 100.0m, } ,
                        new Product { Id = 2, Name = "Bulb", Category = "Lighting", Price = 19.99m, } ,
                        new Product { Id = 3, Name = "Chair", Category = "Furniture", Price = 50.0m, } ,
                        new Product { Id = 4, Name = "Table", Category = "Furniture", Price = 75.8m, } ,
                    }
                ).AsQueryable();
            }
        }

        [EnableQuery]
        public IQueryable<Product> Get()
        {
            var env = GetEnvironment() ?? "no env";

            var products = Products;

            // echo back the env value in the product name
            products.ToList().ForEach(p => p.Name = env + " " + p.Name);

            return products;
        }

        private string GetEnvironment()
        {
            IEnumerable<string> env;
            Request.Headers.TryGetValues("env", out env);

            if (env == null) return null;

            return env.FirstOrDefault();
        }

        [EnableQuery]
        public IQueryable<Product> Get([FromODataUri] int key)
        {
            var result = Products.Where(m => m.Id == key);
            return result;
        }

    }
}

