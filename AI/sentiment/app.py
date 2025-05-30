from flask import Flask, request, jsonify
from flask_mysqldb import MySQL
from sentiment_api import analyze_sentiment

app = Flask(__name__)

app.config['MYSQL_HOST'] = 'localhost'
app.config['MYSQL_USER'] = 'root'
app.config['MYSQL_PASSWORD'] = 'Admin@123'
app.config['MYSQL_DB'] = 'alumnisocialnetwork'

mysql = MySQL(app)

@app.route("/analyze", methods=["POST"])
def analyze():
    data = request.get_json()
    id_comment = data.get("id", "")
    text = data.get("text", "")

    if not text:
        return jsonify({"error": "Missing text"}), 400

    result = analyze_sentiment(text)
    label = result["label"]
    score = round(result["score"], 4)

    # try:
    #     cursor = mysql.connection.cursor()
    #     cursor.execute("UPDATE comment SET label = %s WHERE id = %s", (label, id_comment))
    #     mysql.connection.commit()
    #     cursor.close()
    # except Exception as e:
    #     return jsonify({"error": str(e)}), 500

    return jsonify({
        "label": label,
        "score": score
    })  

if __name__ == "__main__":
    app.run(port=5001)
