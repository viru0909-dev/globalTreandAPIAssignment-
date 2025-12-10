# Deployment Guide - Global Trend API Integration

This guide will help you deploy the application to Render (free tier).

## Prerequisites

- GitHub account (you already have the repo)
- Render account (create free at https://render.com)

## Step-by-Step Deployment to Render

### 1. Create Render Account

1. Go to https://render.com
2. Click "Get Started for Free"
3. Sign up with GitHub (recommended) or email

### 2. Connect Your GitHub Repository

1. After signing in, click "New +" in top right
2. Select "Web Service"
3. Click "Connect account" to connect your GitHub
4. Authorize Render to access your repositories
5. Find and select `globalTreandAPIAssignment-` repository

### 3. Configure the Web Service

Render will auto-detect your `render.yaml` configuration, but verify these settings:

**Basic Settings:**
- **Name**: `global-trend-api` (or your preferred name)
- **Region**: Oregon (US West) or choose closest to you
- **Branch**: `main`
- **Runtime**: Docker

**Build & Deploy:**
- **Build Command**: (leave empty - Docker handles this)
- **Start Command**: (leave empty - Dockerfile handles this)

**Instance:**
- **Plan**: Free (0.1 CPU, 512 MB RAM)

### 4. Environment Variables (Optional)

These are already set in `render.yaml`, but you can add more if needed:
- `JAVA_OPTS`: `-Xmx512m -Xms256m`
- `SERVER_PORT`: `8080`

### 5. Deploy!

1. Click "Create Web Service"
2. Render will automatically:
   - Clone your repository
   - Build the Docker image (takes 5-10 minutes first time)
   - Deploy the application
3. Watch the deployment logs in real-time

### 6. Access Your Live Application

Once deployed, Render provides a URL like:
```
https://global-trend-api.onrender.com
```

Your application will be live and accessible to anyone!

## Post-Deployment

### Testing Your Deployed App

Test the API endpoints:
```bash
# List GitHub users
curl https://YOUR-APP-URL.onrender.com/api/github/users?since=0&perPage=5

# Get posts
curl https://YOUR-APP-URL.onrender.com/api/posts?limit=5
```

### Important Notes

**Free Tier Limitations:**
- ⚠️ App spins down after 15 mins of inactivity
- ⚠️ First request after sleep takes ~30-60 seconds to wake up
- ⚠️ 750 hours/month free (enough for continuous running)

**For Assignment Submission:**
- Include the live URL in your README
- Mention it spins down on free tier
- First load may be slow, but it's normal

### Updating Your Deployment

When you push changes to GitHub:
```bash
git add .
git commit -m "Update feature"
git push origin main
```

Render will **automatically redeploy** - no manual steps needed! 🚀

## Alternative: Railway Deployment

If you prefer Railway:

1. Go to https://railway.app
2. Sign up with GitHub
3. Click "New Project" → "Deploy from GitHub repo"
4. Select your repository
5. Railway auto-detects Dockerfile and deploys

## Troubleshooting

**Build Fails:**
- Check build logs in Render dashboard
- Ensure all files are committed to GitHub
- Verify Dockerfile syntax

**App Not Starting:**
- Check application logs
- Verify port 8080 is used
- Check memory limits (512MB on free tier)

**API Calls Failing:**
- GitHub API rate limits (60 requests/hour)
- Check external API status
- Review application logs

## Support

- Render Docs: https://render.com/docs
- Community: https://community.render.com

---

**Pro Tip:** Add your live deployment URL to your assignment submission to impress the reviewers! 🌟
