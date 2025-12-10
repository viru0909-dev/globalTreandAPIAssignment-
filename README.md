# 🌐 Global Trend - API Integration Assignment

A Java full-stack web application that integrates with public REST APIs (GitHub and JSONPlaceholder) to fetch, cache, filter, and display data with a modern, premium user interface.

## 🚀 Live Demo

**Deployed Application**: https://global-trend-api.onrender.com

> **Note**: Deployed on Render's free tier. The app may take 30-60 seconds to wake up on first request after 15 minutes of inactivity. The GitHub API section may show rate limit errors (60 requests/hour). **Use the Posts API section for best demo experience.**

---

## 📋 Assignment Overview

This project demonstrates:
- Integration with **multiple public REST APIs** (GitHub API & JSONPlaceholder API)
- **Data caching** for improved performance
- **Filtering and pagination** capabilities
- **Comprehensive error handling** (network failures, timeouts, invalid responses)
- **Clean REST API endpoints** for data access
- **Modern, responsive frontend** with premium design

## 🛠️ Technology Stack

### Backend
- **Java 17**
- **Spring Boot 3.2** - Web framework
- **Spring WebFlux** - WebClient for API integration
- **Spring Cache** - In-memory caching
- **Lombok** - Reduce boilerplate code
- **Maven** - Build tool

### Frontend
- **HTML5** - Structure
- **Vanilla CSS** - Premium glassmorphism design with animations
- **JavaScript (ES6+)** - Dynamic interactions and API calls

### APIs Integrated
1. **GitHub API** - https://api.github.com
   - `/users` - List users endpoint
   - `/users/{username}` - Get user details endpoint
   - `/users/{username}/repos` - Get user repositories endpoint

2. **JSONPlaceholder API** - https://jsonplaceholder.typicode.com
   - `/posts` - List posts endpoint
   - `/posts/{id}` - Get post details endpoint
   - `/users` - List users endpoint
   - `/users/{id}` - Get user details endpoint

## 🚀 Setup Instructions

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- Internet connection (for API calls)

### Installation & Running

1. **Clone the repository**
   ```bash
   git clone https://github.com/viru0909-dev/globalTreandAPIAssignment-.git
   cd globalTreandAPIAssignment-
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Open your browser and navigate to: `http://localhost:8080`

The application should start on port 8080 and display a startup message in the console.

## 🌐 Deployment

### Live Demo (Optional)

Want to deploy this application to the cloud? See [DEPLOYMENT.md](DEPLOYMENT.md) for detailed instructions.

**Quick Deploy to Render:**
1. Create account at https://render.com
2. Connect your GitHub repository
3. Render auto-detects the `Dockerfile` and deploys
4. Get a live URL like: `https://your-app.onrender.com`

**Note:** The free tier spins down after 15 minutes of inactivity. First request may take 30-60 seconds to wake up.

---

## 📡 API Endpoints

### GitHub API Endpoints

| Method | Endpoint | Description | Query Parameters |
|--------|----------|-------------|------------------|
| GET | `/api/github/users` | List GitHub users | `since` (default: 0), `perPage` (default: 10) |
| GET | `/api/github/users/{username}` | Get specific user details | - |
| GET | `/api/github/users/{username}/repos` | Get user repositories | `page` (default: 1), `perPage` (default: 10) |

### JSONPlaceholder API Endpoints

| Method | Endpoint | Description | Query Parameters |
|--------|----------|-------------|------------------|
| GET | `/api/posts` | List all posts | `userId` (optional), `limit` (optional) |
| GET | `/api/posts/{id}` | Get specific post details | - |
| GET | `/api/users` | List all users | - |
| GET | `/api/users/{id}` | Get specific user details | - |

### Sample API Requests

```bash
# Get GitHub users
curl http://localhost:8080/api/github/users?since=0&perPage=5

# Get specific GitHub user
curl http://localhost:8080/api/github/users/torvalds

# Get user's repositories
curl http://localhost:8080/api/github/users/torvalds/repos?page=1&perPage=10

# Get posts (all)
curl http://localhost:8080/api/posts

# Get posts by specific user
curl http://localhost:8080/api/posts?userId=1&limit=5

# Get specific post
curl http://localhost:8080/api/posts/1

# Get all users
curl http://localhost:8080/api/users

# Get specific user
curl http://localhost:8080/api/users/1
```

### Sample API Response

```json
{
  "success": true,
  "message": "Request successful",
  "data": [...],
  "metadata": {
    "count": 10,
    "since": 0,
    "perPage": 10
  }
}
```

## 🎯 Features Implemented

### 1. Multiple API Integration ✅
- Integrated with **4 different endpoints** across 2 public APIs
- GitHub API: users, user details, repositories
- JSONPlaceholder API: posts, users

### 2. Data Caching ✅
- In-memory caching using Spring Cache
- Separate cache for each endpoint
- Reduces API calls and improves performance

### 3. Filtering & Pagination ✅
- **GitHub Users**: Filter by `since` (starting ID) and `perPage`
- **GitHub Repos**: Pagination with `page` and `perPage`
- **Posts**: Filter by `userId` and `limit` results

### 4. Error Handling ✅
Comprehensive error handling for:
- **Network failures** - Connection errors to external APIs
- **Timeouts** - Request timeout after 10 seconds
- **Invalid responses** - Malformed or unexpected API responses
- **Resource not found** - Invalid IDs or usernames
- **HTTP errors** - Various HTTP status codes

### 5. Clean Output ✅
- **List View**: Grid of cards with key information
- **Detail View**: Comprehensive view when clicking on items
- **Filtering UI**: Easy-to-use filters for data
- **Error Display**: User-friendly error messages

### 6. Modern UI ✅
- **Glassmorphism design** with dark theme
- **Smooth animations** and transitions
- **Responsive layout** for mobile and desktop
- **Premium aesthetic** with gradient accents
- **Loading states** and spinners

## 📁 Project Structure

```
globalTreandAPIAssignment-/
├── src/
│   ├── main/
│   │   ├── java/com/globaltrend/api/
│   │   │   ├── GlobalTrendApiApplication.java    # Main application class
│   │   │   ├── config/
│   │   │   │   ├── WebClientConfig.java          # WebClient configuration
│   │   │   │   └── CacheConfig.java              # Cache configuration
│   │   │   ├── controller/
│   │   │   │   ├── GitHubController.java         # GitHub API endpoints
│   │   │   │   └── PostsController.java          # Posts API endpoints
│   │   │   ├── service/
│   │   │   │   ├── GitHubApiService.java         # GitHub API integration
│   │   │   │   └── JsonPlaceholderService.java   # JSONPlaceholder integration
│   │   │   ├── model/
│   │   │   │   ├── GitHubUser.java               # GitHub user model
│   │   │   │   ├── GitHubRepository.java         # GitHub repo model
│   │   │   │   ├── Post.java                      # Post model
│   │   │   │   ├── User.java                      # User model
│   │   │   │   ├── ApiResponse.java               # API response wrapper
│   │   │   │   └── ErrorResponse.java             # Error response model
│   │   │   └── exception/
│   │   │       ├── GlobalExceptionHandler.java    # Global error handler
│   │   │       ├── ApiIntegrationException.java   # Custom exception
│   │   │       └── ResourceNotFoundException.java # Not found exception
│   │   └── resources/
│   │       ├── application.yml                    # Application configuration
│   │       └── static/
│   │           ├── index.html                     # Main HTML page
│   │           ├── css/
│   │           │   └── styles.css                 # Premium styling
│   │           └── js/
│   │               └── app.js                     # Frontend JavaScript
├── pom.xml                                        # Maven configuration
├── Dockerfile                                     # Docker containerization
├── render.yaml                                    # Render deployment config
├── .dockerignore                                  # Docker build exclusions
├── .gitignore                                     # Git ignore rules
├── README.md                                      # This file
└── DEPLOYMENT.md                                  # Deployment guide
```

## 🔍 Key Implementation Details

### Caching Strategy
- Uses Spring's `@Cacheable` annotation
- Cache names: `github-users`, `github-repos`, `posts`, `users`
- Cache keys based on request parameters
- Automatic cache invalidation on application restart

### Error Handling
- Global exception handler using `@RestControllerAdvice`
- Specific handlers for different error types
- Consistent error response format
- Detailed logging for debugging

### API Integration
- WebClient with custom timeout (10 seconds)
- Retry logic for transient failures
- Response validation
- Header configuration (User-Agent for GitHub)

## 🎨 UI Features

### GitHub Users Section
- Browse GitHub users with pagination
- Click on a user to view detailed profile
- View user's public repositories
- Visual stats (followers, repos, etc.)

### Posts Section
- View all posts or filter by user ID
- Limit number of results
- Click on a post to read full content
- Navigate to user details from posts
- View all users from JSONPlaceholder

## 📝 Assumptions & Notes

1. **API Rate Limits**: GitHub API has rate limits (60 requests/hour unauthenticated). Caching helps mitigate this.

2. **Security**: No authentication is implemented as this is a demonstration project using public APIs.

3. **Data Storage**: Uses in-memory caching only. Data is not persisted to a database.

4. **CORS**: Configured to allow requests from the frontend (same origin).

5. **Timeouts**: Set to 10 seconds for all external API calls.

6. **Browser Compatibility**: Tested on modern browsers (Chrome, Firefox, Safari, Edge).

## 🧪 Testing the Application

1. **Start the application** using `mvn spring-boot:run`

2. **Open browser** to `http://localhost:8080`

3. **Test GitHub API**:
   - Click "Fetch Users" to load GitHub users
   - Click on a user card to view details
   - View their repositories
   - Try different pagination values

4. **Test Posts API**:
   - Switch to "Posts API" tab
   - Click "Fetch Posts" to load posts
   - Try filtering by User ID (1-10)
   - Click "View Users" to see all users
   - Click on items for detailed views

5. **Test Error Handling**:
   - Try accessing `/api/github/users/this-user-definitely-does-not-exist-123456`
   - Observe the error handling and user feedback

## 🚀 Deployment

The application can be deployed to:
- **Heroku**: Using the Spring Boot Maven plugin
- **AWS Elastic Beanstalk**: Upload JAR file
- **Docker**: Create a Dockerfile and deploy to any container platform
- **Any Java hosting service** that supports Spring Boot

## 👨‍💻 Developer

Created by **Virender Gadekar** for the GLOBAL TREND API Integration Internship Assignment.

## 📄 License

This project is created for educational and assessment purposes.
