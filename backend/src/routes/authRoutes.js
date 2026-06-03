import express from 'express';
import { register, login, getProfile } from '../controllers/authController.js';
import authenticateToken from '../middleware/auth.js';

const router = express.Router();

// Register
router.post('/register', register);

// Login
router.post('/login', login);

// Get Profile (requires auth)
router.get('/profile', authenticateToken, getProfile);

export default router;
