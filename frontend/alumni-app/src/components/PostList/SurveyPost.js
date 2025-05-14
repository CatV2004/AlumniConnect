import React from 'react';

const SurveyPost = ({ survey }) => {
  return (
    <div className="p-4 border-t border-gray-100 bg-blue-50">
      <div className="mb-2">
        <h3 className="font-semibold text-blue-700">Survey: {survey.surveyType}</h3>
        <p className="text-sm text-gray-600">Ends on {new Date(...survey.endTime).toLocaleDateString()}</p>
      </div>
      
      {survey.questions?.map((question, qIndex) => (
        <div key={qIndex} className="mb-4">
          <p className="font-medium mb-2">{question.question}</p>
          <div className="space-y-2">
            {question.options?.map((option, oIndex) => (
              <div key={oIndex} className="flex items-center">
                <input 
                  type={question.multiChoice ? "checkbox" : "radio"} 
                  id={`q${qIndex}-o${oIndex}`}
                  name={`question-${qIndex}`}
                  className="mr-2"
                />
                <label htmlFor={`q${qIndex}-o${oIndex}`}>{option.optionText}</label>
              </div>
            ))}
          </div>
        </div>
      ))}
      
      <button className="mt-2 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">
        Submit Survey
      </button>
    </div>
  );
};

export default SurveyPost;