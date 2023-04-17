import React from "react";
import Link from "next/link";

function Product({product}) {
    return (
        <React.Fragment>
            <Link href={`/products/${product.ean}`}>
                <div title="product-container" className="border border-gray-400 shadow-md rounded-md">
                    <div id="image-container">
                        <img id="product-img" src="http://i.imgur.com/7Vbe8z9.jpg" alt="product-image"
                             className="rounded-md shadow-md brightness-50 hover:brightness-100 hover:transition 1s ease-in-out delay-100 "></img>
                    </div>
                    <div id="description-container" className="flex flex-col p-2 text-left bg-gray-100">
                        <p className="text-sm text-gray-500 font-bold text-left">{product.brand !== undefined || "SSC Napoli"}</p>
                        <p className="text-base text-left ">{product.model !== undefined || "Bomba Maradona"}</p>
                    </div>
                </div>
            </Link>

        </React.Fragment>
    );
}

export default React.memo(Product);