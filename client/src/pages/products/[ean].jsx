import React from "react";
import Link from "next/link";
import Header from "@/components/Header";

export default function SpecificProductPage() {
    return (
        <React.Fragment>
            <Header/>
            <div id="product-information" className="grid grid-cols-4 grid-rows-2 p-10 gap-x-3 gap-y-3">
                <div id="first-row" className="row-span-1 col-span-2">
                    <p className="text-2xl font-bold">SSC Napoli</p>
                    <p className="text-xl">Bomba</p>
                </div>
                <img src="http://i.imgur.com/7Vbe8z9.jpg" alt="product-image" id="product-image" className="col-span-1"/>
                <div id="product-description" className="col-span-3">
                    <p>a</p>
                </div>
            </div>
        </React.Fragment>
    );
}