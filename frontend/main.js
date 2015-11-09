/**
 * Frontend component of the switch project
 * @author Onur Temizkan
 * @version 1.0.0
 */
(function() {
  "use strict";

  /**
   * Configuration
   */
  var conf = function() {
    return {
      request: {
        method: "GET",
        url: "http://localhost:9000/"
      }
    };
  };

  /**
   * XHR Request to given url with given method
   * @param {string} method - The method to be used to make request
   * @param {string} url - The url of the server to be requested
   * @returns {Promise} returns the promise for the result of the request
   */
  var makeRequest = function(method, url) {
    return new Promise(function(resolve, reject) {
      var xhr = new XMLHttpRequest();
      xhr.open(method, url);
      xhr.onload = function() {
        if (this.status >= 200 && this.status < 300) {
          resolve(xhr.response);
        } else {
          reject({
            status: this.status,
            statusText: xhr.statusText
          });
          // Rejected!
        }
      };
      xhr.onerror = function() {
        reject({
          status: this.status,
          statusText: xhr.statusText
        });
      };
      xhr.send();
    });
  };

  /**
   * Finds element from its project name
   * @param {string} projectName - Name of the project to be found
   * @param {array} projectList - Array of projects to be searched
   */
  var findElement = function(projectName, projectList) {
    var ret = retList.find(function(element, index, array) {
      if (element.name == projectName) {
        return true;
      }
      return false;      
    });  
    if (ret != undefined) {
      return ret.isActive;
    }
    return "Node Not Found!";
  };
    
  
  /**
   * Calls an XHR request and checks if the given project is active 
   * @param {string} projectName - Name of the project to be found
   */
  var checkIfActive = function(projectName) {
    makeRequest(conf().request.method, conf().request.url)
      .then(function(data) {
        var retList = [];
        JSON.parse(data).forEach(function(node) {
          retList.push(node);
        });
        return findElement(projectName, retList);
      });
  };
})();
