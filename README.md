# News Categorization App

This project fetches, categorizes, and stores news articles from various sources into MongoDB. It utilizes the NewsAPI to gather news headlines and uses a pre-trained model from Hugging Face to categorize news into topics like Technology, Sports, Health, and AI.

## Features
- Fetches news from NewsAPI for various categories.
- Categorizes news articles using Hugging Face's pre-trained model.
- Stores categorized news in different MongoDB collections based on category.

## Technologies Used
- **Java** for backend processing.
- **NewsAPI** for fetching news headlines.
- **Hugging Face's BART model** for text classification (using `https://huggingface.co/facebook/bart-large-mnli`).
- **MongoDB** for data storage.

## Getting Started

### Prerequisites
- Java 11 or higher
- MongoDB (either local or cloud)
- Hugging Face API Token (required to use the pre-trained model)

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/KalanaBimsara/News_Artical_Recommendation_system.git
   cd News_Artical_Recommendation_system
