import React from "react";
import Product from "@/components/product";

export default function Index() {
    const [products, setProducts] = React.useState([]);
    const [productName, setProductName] = React.useState("");



    function handleButtonClick() {

    }

    return (
        <React.Fragment>
            <div id="product-page-header" className="flex flex-row justify-evenly">
                <input id="product-search" type="text" placeholder="Search a product by its name"
                       className="rounded-md bg-gray-100 p-2"
                       onChange={(e) => setProductName(e.target.value)}>
                </input>
                <button onClick={() => filterProducts()} id="product-search-button" name="Search">Search</button>
                <button onClick={() => handleButtonClick()} id="return-to-homepage-button" name="Back to homepage">Back
                    to Homepage
                </button>
            </div>
            <div title="grid-container" className="grid grid-cols-4 p-10 gap-x-3 gap-y-3 text-center">
                <Product/>
                <Product/>
                <Product/>
                <Product/>
            </div>
        </React.Fragment>


    );
}