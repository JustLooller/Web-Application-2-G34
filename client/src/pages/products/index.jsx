import React from "react";
import Product from "@/components/product";
import Link from "next/link";
import {ProductDTO} from "@/utils/product";

export default function Index() {
    const [products, setProducts] = React.useState([]);


    async function getServerSideProps() {
        await ProductDTO.getAllProducts().then((res) => {
            setProducts(res);
        });
    }

    function filterProducts() {
        const productName = document.getElementById("product-search").value;
        if (productName === "") {
            getServerSideProps();
        } else {
            const filteredProducts = products.filter((product) => {
                return product.name.toLowerCase().startsWith(productName.toLowerCase());
            });
            setProducts(filteredProducts);
        }
    }

    return (
        <React.Fragment>
            <div id="header-links-container" className="flex justify-end">
                <Link href="/" id="return-to-homepage-button" name="Back to homepage"
                      className="bg-gray-100 rounded-md shadow-md px-2 py-2 mr-10 mt-5 hover:bg-gray-200 active:bg-gray-300 focus:outline-none focus:ring focus:ring-gray-400">Back
                    to Homepage
                </Link>
            </div>
            <div id="product-page-header" className="flex flex-row justify-center">
                <input id="product-search" type="text" placeholder="Search for a product"
                       className="rounded-md bg-gray-100 py-1 px-2 shadow-md focus:outline-none active:bg-gray-200 focus:ring focus:ring-gray-400"
                       onChange={(e) => setProductName(e.target.value)}>
                </input>
                <button onClick={() => filterProducts()} id="product-search-button" name="Search"
                        className="bg-gray-100 rounded-md shadow-md ml-3 py-1 px-2 hover:bg-gray-200 active:bg-gray-300 focus:outline-none focus:ring focus:ring-gray-400">Search
                </button>
            </div>
            <div title="grid-container" className="grid grid-cols-4 p-10 gap-x-3 gap-y-3 text-center">
                {products.map((product) => {
                    return (
                        <Product key={product.ean} product={product}/>
                    )
                })}
            </div>
        </React.Fragment>


    );
}