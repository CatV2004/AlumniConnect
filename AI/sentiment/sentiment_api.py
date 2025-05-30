
import torch
from transformers import RobertaForSequenceClassification, AutoTokenizer

model = RobertaForSequenceClassification.from_pretrained("wonrax/phobert-base-vietnamese-sentiment")

tokenizer = AutoTokenizer.from_pretrained("wonrax/phobert-base-vietnamese-sentiment", use_fast=False)

def analyze_sentiment(text="cái bài post như cái quần que"):
    input_ids = torch.tensor([tokenizer.encode(text)])
    with torch.no_grad():
        outputs = model(input_ids)
        probs = torch.softmax(outputs.logits, dim=-1)[0]

    labels = ["NEGATIVE", "POSITIVE", "NEUTRAL"]
    predicted_index = torch.argmax(probs).item()
    return {
        "label": labels[predicted_index],
        "score": probs[predicted_index].item()
    }
