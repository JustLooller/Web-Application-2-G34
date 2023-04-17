import React, {useEffect} from "react";
import Product from "../../components/product";
import {ProductDTO} from "../../utils/product";
import {Link} from "react-router-dom";

export default function Index() {
    const [products, setProducts] = React.useState([]);
    const [productName, setProductName] = React.useState("");


    useEffect(() => {
        async function getData(){
            let res = await ProductDTO.getAllProducts();
            setProducts(res);
        }
        getData()
    },[]);

    return (
        <React.Fragment>
            <div id="header-links-container" className="flex justify-end">
                <Link to="/" id="return-to-homepage-button" name="Back to homepage"
                      className="bg-gray-100 rounded-md shadow-md px-2 py-2 mr-10 mt-5 hover:bg-gray-200 active:bg-gray-300 focus:outline-none focus:ring focus:ring-gray-400">Back
                    to Homepage
                </Link>
            </div>
            <div id="product-page-header" className="flex flex-row justify-center">
                <input id="product-search" type="text" placeholder="Search for a product"
                       className="rounded-md bg-gray-100 py-1 px-2 shadow-md focus:outline-none active:bg-gray-200 focus:ring focus:ring-gray-400"
                       onChange={(e) => setProductName(e.target.value)}>
                </input>
            </div>
            <div title="grid-container" className="grid grid-cols-4 p-10 gap-x-3 gap-y-3 text-center">
                {products.filter((it) => it.name.startsWith(productName)).map((product) => {
                    return (
                        <Product key={product.ean} product={product}/>
                    )
                })}
            </div>
        </React.Fragment>


    );
}