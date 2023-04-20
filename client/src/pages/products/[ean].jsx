import React, {useEffect} from "react";
import Header from "../../components/Header";
import {useParams} from "react-router-dom";
import {ProductDTO} from "../../utils/product";

export default function SpecificProductPage() {
    const [data, setData] = React.useState({});

    const {ean} = useParams();

    useEffect(() => {
        async function getData() {
            let res = await ProductDTO.getProductDetails(ean);
            setData(res);
        }

        getData()
    }, [ean]);


    return (
        <React.Fragment>
            <Header/>
            <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
                <div className="flex flex-col md:flex-row">
                    <div className="md:w-1/2">
                        <img src={data.image} alt="Product"/>
                    </div>
                    <div className="md:w-1/2 md:pl-6">
                        <h2 className="text-xl font-medium text-gray-500 mb-2">{data.brand}</h2>
                        <h1 className="text-3xl font-bold mb-4">{data.name}</h1>
                        <p className="text-lg font-light mb-4">{data.description}</p>
                        <p className="text-gray-600 text-sm mb-4">{`EAN: ${ean}`}</p>
                        {/* <button className="bg-gray-100 rounded-md shadow-md px-2 py-2 mr-10 mt-5 hover:bg-gray-200 active:bg-gray-300 focus:outline-none focus:ring focus:ring-gray-400">
                        Request Assistance
                    </button> */}
                    </div>
                </div>
            </div>
        </React.Fragment>
    );
};
