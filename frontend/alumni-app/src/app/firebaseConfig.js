// Import the functions you need from the SDKs you need
import { initializeApp } from "firebase/app";
import { enableNetwork, getFirestore } from "firebase/firestore";
// TODO: Add SDKs for Firebase products that you want to use
// https://firebase.google.com/docs/web/setup#available-libraries

// Your web app's Firebase configuration
// For Firebase JS SDK v7.20.0 and later, measurementId is optional
const firebaseConfig = {
  apiKey: "AIzaSyDX794A2b4CK6tiQQGeV7wmnymN7BIIb0w",
  authDomain: "alumniconnect-28a75.firebaseapp.com",
  projectId: "alumniconnect-28a75",
  storageBucket: "alumniconnect-28a75.firebasestorage.app",
  messagingSenderId: "782105225794",
  appId: "1:782105225794:web:754f2a7be09a60783c5bdb",
  measurementId: "G-0WCMGZ6BKR"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
const db = getFirestore(app);

enableNetwork(db)
  .then(() => console.log("Network enabled"))
  .catch((error) => console.error("Error enabling network:", error));

export { db };
