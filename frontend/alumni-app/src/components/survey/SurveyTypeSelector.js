import { BarChart2, Book, Briefcase, DollarSign } from "lucide-react";

const SurveyTypeSelector = ({ value, onChange }) => {
  const surveyTypes = [
    {
      value: "TRAINING_PROGRAM",
      label: "Chương trình đào tạo",
      icon: <Book className="mr-2" size={18} />,
      description: "Khảo sát về chất lượng đào tạo, chương trình học",
    },
    {
      value: "RECRUITMENT_INFORMATION",
      label: "Thông tin tuyển dụng",
      icon: <Briefcase className="mr-2" size={18} />,
      description: "Khảo sát nhu cầu tuyển dụng, thị trường lao động",
    },
    {
      value: "INCOME",
      label: "Thu nhập",
      icon: <DollarSign className="mr-2" size={18} />,
      description: "Khảo sát mức lương, phúc lợi ngành nghề",
    },
    {
      value: "EMPLOYMENT_SITUATION",
      label: "Tình hình việc làm",
      icon: <BarChart2 className="mr-2" size={18} />,
      description: "Khảo sát tỷ lệ có việc làm sau tốt nghiệp",
    },
  ];

  return (
    <div className="space-y-4">
      <label className="block text-sm font-medium text-gray-700 mb-2">
        Loại khảo sát <span className="text-red-500">*</span>
      </label>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
        {surveyTypes.map((type) => (
          <div
            key={type.value}
            onClick={() => onChange(type.value)}
            className={`p-4 border rounded-lg cursor-pointer transition-all ${
              value === type.value
                ? "border-blue-500 bg-blue-50 ring-1 ring-blue-500"
                : "border-gray-300 hover:border-blue-300"
            }`}
          >
            <div className="flex items-center">
              <div
                className={`p-2 rounded-full ${
                  value === type.value
                    ? "bg-blue-100 text-blue-600"
                    : "bg-gray-100 text-gray-600"
                }`}
              >
                {type.icon}
              </div>
              <h4 className="ml-3 font-medium">{type.label}</h4>
            </div>
            <p className="mt-2 text-sm text-gray-500 ml-11">
              {type.description}
            </p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default SurveyTypeSelector;
