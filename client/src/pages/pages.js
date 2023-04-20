import Home from './index'
import ProductSearchInsertPage from './products/index'
import ProductDetailPage from './products/[ean]'

import ProfileSearchInsertPage from './profiles/index'
import ProfileDetailPage from './profiles/[email]'
import ErrorPage from "../components/ErrorPage";

const PAGES = [
    {
        'path': '/',
        'element': <Home/>,
        'errorElement': <ErrorPage />
    },
    {
        'path': '/products',
        'element': <ProductSearchInsertPage/>,
        'errorElement': <ErrorPage />

    },
    {
        'path': '/products/:ean',
        'element': <ProductDetailPage/>,
        'errorElement': <ErrorPage />

    },
    {
        'path': '/profiles',
        'element': <ProfileSearchInsertPage/>,
        'errorElement': <ErrorPage />

    },
    {
        'path': '/profiles/:email',
        'element': <ProfileDetailPage/>,
        'errorElement': <ErrorPage />
    },
    {
        'path': '/error/:message',
        'element': <ErrorPage/>,
    }
]

export default PAGES