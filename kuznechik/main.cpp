#include <iostream>
using std::cout;
using std::cerr;
using std::endl;

#include <fstream>

#include <string>
using std::string;
using std::getline;

#include <vector>
using std::vector;

#include <limits>
using std::numeric_limits;

#include <stdexcept>

#include "argvparser.h"
using namespace CommandLineProcessing;

#include "Kuznyechik.hpp"
#include "mycrypto.hpp"

vector<string> split(const string & s, char ch) {
    vector<string> v;

    string::size_type i = 0;
    string::size_type j = s.find(ch);
    while(j != string::npos) {
        v.push_back(s.substr(i, j-i));
        i = ++j;
        j = s.find(ch, j);

        if(j == string::npos)
            v.push_back(s.substr(i, s.size()-i));
    }
    return v;
}

vector<string> read_cipher_params(const string & filename) {
	std::ifstream fin(filename);
	if(!fin.is_open()) {
		throw std::invalid_argument("Cannot open source file");
	}
	vector<string> result, params {"KEY", "IV", "INPUT"};
	for(auto & p : params) {
		string tmp;
		getline(fin, tmp);
		auto line = split(tmp, '=');
		if(line.size() != 2 || line[0] != p)
			throw std::invalid_argument("Bad file");
		result.push_back(line[1]);
	}
	fin.close();
	return result;
}

bool logic_xor3(bool x, bool y, bool z) {
    return (!x && !y && z) || (!x && y && !z) || (x && !y && !z);
}

int main(int argc, char ** argv) {
	try {
		ArgvParser cmd;

		cmd.setIntroductoryDescription("Encryptor with Kuznyechik using CFB mode of encryption.");
		cmd.setHelpOption("h", "help", "Print this message");

		cmd.defineOption("path", "File with data to be processed on",
			ArgvParser::OptionRequired | ArgvParser::OptionRequiresValue);
		cmd.defineOption("dest", "File to write into",
			ArgvParser::OptionRequiresValue);
		cmd.defineOption("encrypt", "Encrypt the message");
		cmd.defineOption("decrypt", "Decrypt the message");

        cmd.defineOption("parallel-decrypt", "Decrypt the message with parallelizm");

		cmd.defineOptionAlternative("path", "P");
		cmd.defineOptionAlternative("dest", "D");
		cmd.defineOptionAlternative("encrypt", "e");
		cmd.defineOptionAlternative("decrypt", "d");
        cmd.defineOptionAlternative("parallel-decrypt", "l");

		int parsing_result = cmd.parse(argc, argv);
		if(parsing_result) {
			throw std::invalid_argument(
				cmd.parseErrorDescription(parsing_result)
			);
		}

		string src_filename = cmd.optionValue("path");
		string dst_filename;
		if(cmd.foundOption("dest")) {
			dst_filename = cmd.optionValue("dest");
		} else {
			dst_filename = "kuznyechik_cfb_output.txt";
		}

        if( !logic_xor3(
                cmd.foundOption("encrypt"),
                cmd.foundOption("decrypt"),
                cmd.foundOption("parallel-decrypt")) )
        {
            throw std::invalid_argument(
                "Only and at least one action must be present"
            );
        }

		auto cipher_params = read_cipher_params(src_filename);
		ByteBlock key = hex_to_bytes(cipher_params[0]);
		ByteBlock iv = hex_to_bytes(cipher_params[1]);
		ByteBlock message = hex_to_bytes(cipher_params[2]);
		ByteBlock output;

		CFB_Mode<Kuznyechik> encryptor(Kuznyechik(key), iv);


		if(cmd.foundOption("encrypt")) {
			encryptor.encrypt(message, output);
		} else if(cmd.foundOption("decrypt")) {
            encryptor.decrypt(message, output);
		} else if(cmd.foundOption("parallel-decrypt")) {
            encryptor.parallel_decrypt(message, output);
        } else {
            output = std::move(message);
        }

		std::ofstream fout(dst_filename);
		if(!fout.is_open()) {
			throw std::invalid_argument("Cannot open destiantion file");
		}
		fout << "KEY=" << cipher_params[0] << endl;
		fout << "IV=" << cipher_params[1] << endl;
		fout << "INPUT=" << cipher_params[2] << endl;
		fout << "OUTPUT=" << hex_representation(output) << endl;
		std::cout << hex_representation(output);
		fout.close();
	} catch(const std::exception & e) {
		cerr << "Error: " << e.what() << endl;
		cerr << "For help type: " << endl << argv[0] << " --help" << endl;
		return 1;
	}

	return 0;
}
