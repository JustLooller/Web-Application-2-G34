import React, {useMemo} from "react";
import Image from "next/image";

function Product(props) {
  return (
      <React.Fragment>
          <div title="product-container" className="border border-amber-400 shadow-md rounded-md" >
              <div id="image-container">
                  <img id="product-img" src="http://i.imgur.com/7Vbe8z9.jpg" alt="product-image" className="rounded-md shadow-md brightness-50 hover:brightness-100 hover:transition 1s ease-in-out delay-100 "></img>
              </div>
              <div id="description-container" className="flex flex-col p-2 text-left bg-gray-100">
                  <h3 className="text-3xl font-bold text-center">Goku Napoli</h3>
                  <p className="">Goku</p>
                  <p className="font-thin ">Description: Goku</p>
              </div>
          </div>
      </React.Fragment>
  );
}

export default React.memo(Product);