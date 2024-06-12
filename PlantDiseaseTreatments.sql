CREATE USER 'admin'@'localhost' IDENTIFIED BY '1234';
GRANT ALL PRIVILEGES ON PlantDiseaseDB.* TO 'admin'@'localhost';
FLUSH PRIVILEGES;

-- Create the database
CREATE DATABASE IF NOT EXISTS PlantDiseaseDB;
USE PlantDiseaseDB;

-- Create table for Tomato diseases
CREATE TABLE IF NOT EXISTS TomatoDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Corn diseases
CREATE TABLE IF NOT EXISTS CornDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Apple diseases
CREATE TABLE IF NOT EXISTS AppleDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Grapes diseases
CREATE TABLE IF NOT EXISTS GrapesDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Cherry diseases
CREATE TABLE IF NOT EXISTS CherryDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Potato diseases
CREATE TABLE IF NOT EXISTS PotatoDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Strawberry diseases
CREATE TABLE IF NOT EXISTS StrawberryDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Peach diseases
CREATE TABLE IF NOT EXISTS PeachDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Bell Pepper diseases
CREATE TABLE IF NOT EXISTS BellPepperDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Orange diseases
CREATE TABLE IF NOT EXISTS OrangeDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Squash diseases
CREATE TABLE IF NOT EXISTS SquashDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Create table for Soybeans diseases
CREATE TABLE IF NOT EXISTS SoybeansDiseases (
    DiseaseID INT AUTO_INCREMENT PRIMARY KEY,
    DiseaseName VARCHAR(255) NOT NULL,
    SymptomDescription TEXT,
    TreatmentDescription TEXT
);

-- Insert data for Apple diseases
INSERT INTO AppleDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Apple Rust', 'Rusty, orangish-yellow stains on leaves, branches, and fruit. Fungus uses one tree as a host, spreads to others', 'Eliminate the host tree quickly. Spray trees with lime sulfur to fight fungal spores'),
('Apple Scab', 'Dark, scabby lesions on leaves and fruit, causing them to become misshapen', 'Remove fallen leaves in autumn. Apply fungicides in spring when buds begin to open'),
('Black Rot', 'Brown spots on the end of each apple, growing larger and turning black. Holes in infected leaves', 'Prune or burn infected parts. Spray with fungicides such as Captan or Mancozeb to prevent spread'),
('Cedar Apple Rust', 'Small, yellowish spots on leaves in the spring, growing larger as the disease advances. Leaves die prematurely', 'Apply fungicides in early spring. Remove nearby juniper or cedar trees to prevent spread'),
('Fire Blight', 'Wilting and blackening of blossoms, shoots, and branches, often with a shepherd’s crook appearance', 'Prune infected branches during dry weather, disinfecting tools between cuts. Apply copper-based sprays at bloom time'),
('Phytophthora Rot', 'Reddish-brown cankers on the trunk and lower limbs, yellowing leaves', 'Ensure good drainage to prevent wet soil. Apply appropriate fungicides to manage disease'),
('Powdery Mildew', 'White, powdery growth on leaves, stems, and fruit', 'Prune infected shoots. Apply fungicides such as sulfur or neem oil. Improve air circulation around trees'),
('Sooty Blotch', 'Black, sooty spots on the surface of apples, often accompanied by flyspeck', 'Prune trees to improve air circulation. Apply fungicides as needed. Remove affected fruit'),
('White Rot', 'Soft, watery rot on fruit, and white fungal growth on the bark', 'Remove and destroy infected fruit and wood. Apply fungicides to protect trees from infection');

-- Insert data for Bell Pepper diseases
INSERT INTO BellPepperDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Anthracnose', 'Sunken, dark lesions on fruit, often with concentric rings', 'Apply fungicides preventatively. Remove and destroy infected plant parts. Improve air circulation around plants'),
('Bacterial Spot', 'Water-soaked lesions on leaves and fruit, which turn brown or black and become raised', 'Use copper-based bactericides. Avoid working in wet fields to prevent spread. Use disease-free seeds'),
('Phytophthora Blight', 'Dark, water-soaked lesions on stems and fruit. Wilting and death of leaves and branches', 'Improve soil drainage. Apply fungicides preventatively. Avoid overhead irrigation'),
('Powdery Mildew', 'White powdery substance on leaves and stems', 'Remove and destroy infected plant parts. Apply fungicides such as sulfur or potassium bicarbonate. Improve air circulation around plants'),
('Verticillium Wilt', 'Wilting and yellowing of leaves. Browning of vascular tissue in crown and roots', 'Plant resistant cultivars. Avoid planting in fields with a history of Verticillium wilt. Use soil fumigation if necessary');

-- Insert data for Orange diseases
INSERT INTO OrangeDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Citrus Greening', 'Yellowing of leaves along veins, misshapen and bitter-tasting fruit', 'No cure available. Remove and destroy infected trees. Control citrus psyllid vectors. Use healthy planting material'),
('Citrus Tristeza Virus', 'Yellowing and vein clearing on leaves, decline in tree vigor and fruit quality', 'No cure available. Remove and destroy infected trees. Use disease-free planting material. Control aphid vectors'),
('Citrus Canker', 'Raised, corky lesions on leaves, fruit, and stems, often surrounded by a yellow halo', 'Remove and destroy infected plant parts. Apply copper-based bactericides preventatively. Improve air circulation around trees'),
('Melanose', 'Dark, raised lesions on fruit and leaves, which may crack and ooze gum', 'Prune to increase air circulation. Apply fungicides during the growing season. Remove and destroy infected plant parts'),
('Sooty Mold', 'Black, velvety fungal growth on leaves, stems, and fruit, often associated with honeydew excreted by sucking insects', 'Control sucking insect pests such as aphids and scales. Improve air circulation around trees. Remove and destroy infected plant parts'),
('Huanglongbing (Citrus greening)', 'Yellowing of leaves along veins, misshapen and bitter-tasting fruit', 'No cure available. Remove and destroy infected trees. Control citrus psyllid vectors. Use healthy planting material');

-- Insert data for Peach diseases
INSERT INTO PeachDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Brown Rot', 'Brown, circular spots on fruit which rapidly expand and cover the entire fruit', 'Apply fungicides before and after bloom. Maintain good air circulation around trees. Remove mummified fruit'),
('Bacterial Spot', 'Small, angular, water-soaked lesions on leaves, which turn brown and fall out, leaving holes', 'Apply copper-based bactericides before and after bloom. Prune to increase air circulation'),
('Peach Leaf Curl', 'Distorted, puckered, and reddened leaves, which may drop prematurely', 'Apply fungicides such as copper or lime sulfur during dormancy before buds swell'),
('Peach Mosaic Virus', 'Mottling and distortion of leaves, reduced fruit size and quality', 'No cure available. Remove and destroy infected plants. Use virus-free planting material'),
('Peach Rosette Mosaic', 'Mosaic pattern on leaves, leaf distortion, and stunting', 'No cure available. Remove and destroy infected plants. Use virus-free planting material'),
('Peach Scab', 'Olive-green to black spots on fruit and leaves', 'Apply fungicides before and after bloom. Prune to increase air circulation. Remove affected leaves and fruit'),
('Peach Tree Short Life', 'Sudden collapse and death of young trees, often associated with cold injury', 'Avoid planting in poorly drained sites. Use resistant rootstocks. Manage soil moisture carefully'),
('Peach Yellows', 'Yellowing and curling of leaves, premature fruit drop', 'No cure available. Remove and destroy infected trees. Control leafhoppers which can spread the disease');

-- Insert data for Squash diseases
INSERT INTO SquashDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Anthracnose', 'Irregularly shaped lesions on leaves and fruit, which may ooze pinkish spores', 'Apply fungicides preventatively. Remove and destroy infected plant parts. Improve air circulation around plants'),
('Blossom End Rot', 'Dark, sunken spots on the blossom end of fruit, which may enlarge and turn black', 'Maintain consistent soil moisture. Apply calcium to the soil if necessary. Avoid over-fertilization with nitrogen'),
('Downy Mildew', 'Yellow to brown patches on leaves, often with a fuzzy appearance on the undersides', 'Apply fungicides preventatively. Improve air circulation around plants. Avoid overhead irrigation'),
('Fusarium Wilt', 'Wilting and yellowing of leaves, often starting with one side of the plant', 'Plant resistant cultivars. Rotate crops to avoid planting in fields with a history of Fusarium wilt. Improve soil drainage'),
('Powdery Mildew', 'White powdery substance on leaves and stems', 'Remove and destroy infected plant parts. Apply fungicides such as sulfur or potassium bicarbonate. Improve air circulation around plants'),
('Mosaic Virus', 'Mottling, yellowing, and distortion of leaves. Stunted growth', 'There is no cure for cucumber mosaic virus. Control aphid vectors. Remove and destroy infected plants');

-- Insert data for Grapes diseases
INSERT INTO GrapesDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Black Rot', 'Brown spots on leaves, stems, and fruit, which enlarge and become sunken with black margins', 'Prune vines to improve airflow. Apply fungicides such as captan or sulfur. Remove and destroy infected plant parts'),
('Downy Mildew', 'Yellow to brown spots on upper leaf surfaces with fuzzy white to gray growth on the undersides', 'Apply fungicides preventatively. Improve air circulation around plants. Avoid overhead irrigation'),
('Powdery Mildew', 'White powdery growth on leaves and fruit clusters', 'Apply fungicides preventatively. Remove and destroy infected plant parts. Improve air circulation around plants'),
('Botrytis Bunch Rot', 'Gray mold covering fruit clusters, often in humid conditions or after rain', 'Remove and destroy infected fruit clusters. Apply fungicides preventatively. Improve air circulation around plants'),
('Phomopsis Cane and Leaf Spot', 'Small, dark spots on leaves and stems, which enlarge and turn brown', 'Prune vines to improve airflow. Apply fungicides preventatively. Remove and destroy infected plant parts'),
('Black Rot', 'Brown spots on leaves, stems, and fruit, which enlarge and become sunken with black margins', 'Prune vines to improve airflow. Apply fungicides such as captan or sulfur. Remove and destroy infected plant parts'),
('Esca (Black Measles)', 'Yellowing and wilting of leaves, black streaks in vascular tissue, and internal blackening of berries', 'Prune infected vines during the dormant season. Apply fungicides preventatively. Improve vineyard sanitation'),
('Leaf Blight Isariopsis Leaf Spot', 'Small, circular, dark spots on leaves which enlarge and turn brown with a yellow halo', 'Prune vines to improve airflow. Apply fungicides preventatively. Remove and destroy infected plant parts');

-- Insert data for Cherry diseases
INSERT INTO CherryDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Brown Rot', 'Infected fruit develops brown spots and quickly becomes soft and mummified. Fungus forms spores on the surface of the fruit, producing a powdery mass', 'Fungicides applied during bloom, petal fall, and after rain may provide control. Remove mummified fruit and prune to increase air circulation'),
('Cherry Buckskin Disease', 'Circular or irregular brown spots on leaves with yellow halos', 'Fungicide application during the growing season. Prune to improve airflow and remove infected leaves'),
('Cherry Leaf Spot', 'Circular, purple spots on leaves with yellow halos', 'Fungicides applied during the growing season. Prune to improve airflow and remove infected leaves'),
('Powdery Mildew', 'White powdery fungal growth on leaves, shoots, flowers, and fruit surfaces', 'Fungicide application early in the season and repeated as needed. Prune to improve airflow'),
('Cherry Virus Diseases', 'Varied symptoms including leaf mottling, leaf deformation, and reduced fruit quality', 'No cure for viral diseases. Remove and destroy infected plants to prevent spread'),
('Crown Gall', 'Swollen, tumor-like growths on roots or stems near the soil line', 'Preventative measures include using disease-free planting material and avoiding mechanical injuries to plants'),
('Peach Witches’ Broom', 'Abnormal proliferation of small branches resulting in dense clusters of twigs resembling a broom', 'Prune out affected branches and destroy them. Disinfect pruning tools between cuts'),
('Peach Virus Diseases', 'Varied symptoms including leaf mottling, leaf deformation, and reduced fruit quality', 'No cure for viral diseases. Remove and destroy infected plants to prevent spread');

-- Insert data for Potato diseases
INSERT INTO PotatoDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Early Blight', 'Brown spots with concentric rings on leaves', 'Fungicide applications, crop rotation, resistant varieties, and removal of diseased plant material'),
('Late Blight', 'Dark spots on leaves with white fungal growth on the underside', 'Fungicide applications, good drainage, crop rotation, and resistant varieties'),
('Black Leg', 'Soft, dark, and water-soaked lesions on stems', 'Removal of infected plants, fungicide applications, and use of disease-free seed potatoes'),
('Black Dot', 'Small, dark brown spots on tubers', 'Good crop rotation, removal of infected tubers, and use of disease-free seed potatoes'),
('Common Scab', 'Rough, corky patches on tubers', 'Maintain soil pH around 5.2-5.5, avoid excessive irrigation, and use disease-free seed potatoes'),
('Potato Virus Y', 'Stunted growth, yellowing, and necrosis of leaves', 'Use virus-free seed potatoes and control aphid vectors'),
('Powdery Scab', 'Raised, powdery pustules on tubers', 'Avoid heavy, poorly drained soils and use disease-free seed potatoes'),
('Ring Rot', 'Yellowing and wilting of foliage, yellow-brown discoloration and rotting of vascular tissue', 'Destroy infected plants and use disease-free seed potatoes');

-- Insert data for Soybeans diseases
INSERT INTO SoybeansDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Bacterial Blight', 'Small, angular, water-soaked lesions on leaves which turn brown and fall out, leaving holes', 'Plant resistant cultivars. Rotate crops to avoid planting in fields with a history of bacterial blight. Apply copper-based bactericides if necessary'),
('Bacterial Pustule', 'Small, raised pustules on leaves which may ooze bacterial fluid', 'Plant resistant cultivars. Rotate crops to avoid planting in fields with a history of bacterial pustule. Apply copper-based bactericides if necessary'),
('Downy Mildew', 'Yellow to brown patches on leaves, often with a fuzzy appearance on the undersides', 'Apply fungicides preventatively. Improve air circulation around plants. Avoid overhead irrigation'),
('Frogeye Leaf Spot', 'Circular, brown lesions on leaves with a darker border', 'Plant resistant cultivars. Apply fungicides preventatively. Rotate crops to avoid planting in fields with a history of Frogeye Leaf Spot'),
('Phytophthora Root Rot', 'Wilting and yellowing of leaves, often starting with one side of the plant', 'Plant resistant cultivars. Improve soil drainage. Apply appropriate fungicides if necessary');

-- Insert data for Strawberry diseases
INSERT INTO StrawberryDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Anthracnose Fruit Rot', 'Small, dark sunken lesions on fruit surface. Lesions expand and become covered with pinkish spore masses in humid conditions', 'Fungicide application starting at bloom and repeated at intervals according to label instructions. Remove and destroy infected fruit'),
('Botrytis Fruit Rot', 'Gray mold covering fruit surface. Infected tissue turns soft and watery', 'Fungicide application starting at bloom and repeated at intervals according to label instructions. Remove and destroy infected fruit'),
('Crown Rot', 'Reddish-brown lesions on crown and roots. Wilting and death of leaves', 'Improve soil drainage. Plant in raised beds or mounds. Apply fungicides preventatively'),
('Leaf Scorch', 'Angular, water-soaked lesions on leaves. Lesions may turn brown and necrotic. White fungal growth may develop on the underside of leaves', 'Plant disease-resistant cultivars. Apply copper-based fungicides preventatively'),
('Verticillium Wilt', 'Wilting and yellowing of leaves. Browning of vascular tissue in crown and roots', 'Plant resistant cultivars. Avoid planting in fields with a history of Verticillium wilt. Soil fumigation may be necessary in severe cases');

-- Insert data for Tomato diseases
INSERT INTO TomatoDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Bacterial Speck', 'Irregular brown or black spots near leaf margins, on stems, or fruit', 'Copper fungicide applied as preventive'),
('Spider Mites Two Spotted Spider Mite', 'Stem lesions at or near the soil line', 'Solarization, fungicides, biofungicides, soil fumigants'),
('Target Spot', 'Infected unripe fruit spread by aphids', 'Control aphids with natural predators like ladybugs'),
('Tomato Yellow Leaf Curl Virus', 'Plants are stunted or dwarfed, New growth only produced after infection is reduced in size, Leaflets are rolled upwards and inwards', 'There are no treatments against the infection'),
('Bacterial Spot', 'Irregular brown or black spots near leaf margins, on stems, or fruit', 'Copper fungicide applied as preventive'),
('Early Blight', 'Brown spots with concentric rings on leaves', 'Fungicide applications, crop rotation, resistant varieties, and removal of diseased plant material'),
('Late Blight', 'Dark spots on leaves with white fungal growth on the underside', 'Fungicide applications, good drainage, crop rotation, and resistant varieties'),
('Leaf Mold', 'Pale green to yellow patches on leaves, usually starting from the older leaves', 'Apply fungicides preventatively. Improve air circulation around plants. Avoid overhead irrigation'),
('Septoria Leaf Spot', 'Small, circular spots with dark margins and tan or gray centers on leaves', 'Apply fungicides preventatively. Remove and destroy infected plant debris. Improve air circulation around plants'),
('Spider Mites Two Spotted Spider Mite', 'Stippling, yellowing, and bronzing of leaves. Fine webbing on leaves and stems', 'Apply miticides. Increase humidity and use natural enemies like predatory mites. Remove heavily infested leaves'),
('Target Spot', 'Brown to black circular spots on leaves with concentric rings and a yellow halo', 'Apply fungicides preventatively. Remove and destroy infected plant debris. Improve air circulation around plants'),
('Tomato Mosaic Virus', 'Mottled or streaked leaves, stunted growth, and reduced fruit yield', 'There are no treatments against the infection'),
('Tomato Yellow Leaf Curl Virus', 'Plants are stunted or dwarfed, New growth only produced after infection is reduced in size, Leaflets are rolled upwards and inwards', 'There are no treatments against the infection');

-- Insert data for Corn diseases
INSERT INTO CornDiseases (DiseaseName, SymptomDescription, TreatmentDescription) VALUES
('Eyespot', 'Small, translucent lesions surrounded by yellow to purple margins', 'Manage cool, moist weather conditions'),
('Physoderma Brown Spot', 'Circular to oval, straw-colored lesions with dark borders. Lesions may have white centers', 'Rotate crops, manage residue, and avoid excessive moisture'),
('Stalk Rot', 'Soft, wet, decaying areas near the base of the plant. May be accompanied by a foul smell', 'Improve soil drainage. Avoid excessive irrigation. Plant resistant varieties'),
('Stewart’s Wilt', 'Streaks or bands of yellow that run parallel to leaf veins, eventually turning brown and necrotic', 'Plant resistant varieties. Control flea beetles, which can spread the disease'),
('Cercospora Leaf Spot Gray Leaf Spot', 'Small, translucent lesions surrounded by yellow to purple margins', 'Manage cool, moist weather conditions'),
('Common Rust', 'Small, round to elongated, reddish-brown pustules on leaves and stalks', 'Apply fungicides preventatively. Plant resistant varieties. Remove crop residue after harvest'),
('Northern Leaf Blight', 'Long, elliptical lesions with wavy margins, starting from lower leaves and progressing upwards', 'Plant resistant varieties. Apply fungicides preventatively. Rotate crops to avoid planting in fields with a history of Northern Leaf Blight');