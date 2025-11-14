
import authReducer from "./reducer/authReducer";
import postReducer from "./reducer/postReducer"

/**
 * STEPS For StateManagement
 * Submit Action
 * Handle action in it's reducer
 * Register Here -> Reducer
 * 
 */

const { configureStore } = require("@reduxjs/toolkit");

export const store=configureStore({
    reducer:{
        auth:authReducer,
        posts:postReducer
    }
})

